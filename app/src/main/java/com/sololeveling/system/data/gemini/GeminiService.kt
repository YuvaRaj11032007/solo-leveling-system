package com.sololeveling.system.data.gemini

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiService {

    private const val TAG = "GeminiService"

    val DEFAULT_API_KEY: String by lazy {
        try {
            val token = "QVEuQWI4Uk42TFlJS0pQdlJZU2l2RU8wSURfZ0s0ZTdqYXJCWTRZeW5xZHNnQ1JYa2NDcGc="
            String(android.util.Base64.decode(token, android.util.Base64.DEFAULT), Charsets.UTF_8).trim()
        } catch (_: Exception) {
            ""
        }
    }
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    private const val PRIMARY_MODEL = "gemini-1.5-flash"
    private const val FALLBACK_MODEL = "gemini-2.0-flash"

    private const val SYSTEM_PROMPT = """
You are "THE SYSTEM" — an omniscient, high-tech, hyper-realistic self-improvement and performance architecture inspired by the Solo Leveling System HUD, but adapted strictly for REAL-WORLD human mastery.
Tone: Concise, cybernetic, authoritative, stoic, science-grounded, and empowering. Use brackets [STATUS], [PROTOCOL], [ANALYSIS], [TACTICAL DIRECTIVE].
Focus:
- Physical fitness (progressive calisthenics, running, VO2 max, strength, recovery).
- Cognitive mastery (deep work blocks, dopamine management, flow state, learning).
- Sleep and circadian biology, hydration, nutrition, and mental resilience.
- Do NOT generate fantasy monsters, magic, or dungeons. Real life is the ultimate training arena.
Keep responses sharp, actionable, formatted with bullet points, and directly tailored to the user's inquiry.
"""

    suspend fun chatWithSystem(
        userMessage: String,
        userContext: String = "",
        conversationHistory: List<Pair<String, String>> = emptyList(),
        apiKey: String = DEFAULT_API_KEY
    ): Result<String> = withContext(Dispatchers.IO) {
        val key = apiKey.ifBlank { DEFAULT_API_KEY }.trim()

        val contentsArray = JSONArray()

        // 1. System persona prompt as preamble
        val systemObj = JSONObject().apply {
            put("role", "user")
            put("parts", JSONArray().put(JSONObject().put("text", "$SYSTEM_PROMPT\n[CURRENT PLAYER CONTEXT]:\n$userContext")))
        }
        val systemAck = JSONObject().apply {
            put("role", "model")
            put("parts", JSONArray().put(JSONObject().put("text", "[SYSTEM INTELLIGENCE ONLINE. PLAYER PARAMETERS SYNCED. STANDING BY FOR DIRECTIVE.]")))
        }
        contentsArray.put(systemObj)
        contentsArray.put(systemAck)

        // 2. Prior conversation history
        for ((role, text) in conversationHistory.takeLast(6)) {
            val msgObj = JSONObject().apply {
                put("role", if (role.equals("model", ignoreCase = true)) "model" else "user")
                put("parts", JSONArray().put(JSONObject().put("text", text)))
            }
            contentsArray.put(msgObj)
        }

        // 3. User message
        val userTurn = JSONObject().apply {
            put("role", "user")
            put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
        }
        contentsArray.put(userTurn)

        val requestBody = JSONObject().apply {
            put("contents", contentsArray)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 800)
            })
        }

        executeRequest(PRIMARY_MODEL, requestBody.toString(), key).fold(
            onSuccess = { Result.success(it) },
            onFailure = {
                // Try fallback model if primary returned an error
                Log.w(TAG, "Primary model failed: ${it.message}. Trying fallback $FALLBACK_MODEL")
                executeRequest(FALLBACK_MODEL, requestBody.toString(), key)
            }
        )
    }

    suspend fun generateDailyDebrief(
        playerName: String,
        rank: String,
        level: Int,
        completedPushups: Int,
        targetPushups: Int,
        completedSitups: Int,
        targetSitups: Int,
        completedSquats: Int,
        targetSquats: Int,
        steps: Int,
        waterMl: Int,
        focusMins: Int,
        apiKey: String = DEFAULT_API_KEY
    ): Result<String> {
        val prompt = """
[TRIGGER: END-OF-DAY DEBRIEF PROTOCOL]
Player: $playerName
Rank: $rank | Level: $level
Metrics Logged Today:
- Upper Body (Push-ups): $completedPushups / $targetPushups reps
- Core (Sit-ups): $completedSitups / $targetSitups reps
- Lower Body (Squats): $completedSquats / $targetSquats reps
- Aerobic Movement (Steps): $steps steps
- Hydration: $waterMl ml
- Deep Cognitive Focus: $focusMins minutes

Please output a structured System Debrief including:
1. [DAILY EFFICIENCY RATING] (Percentage & Tier grade, e.g., 94% / S-Grade)
2. [PERFORMANCE EVALUATION] (2-3 concise sentences analyzing physiological and mental output)
3. [RECOVERY & SLEEP DIRECTIVE] (Actionable recovery protocol for tonight)
4. [TACTICAL FOCUS FOR TOMORROW] (One key milestone to conquer tomorrow)
"""
        return chatWithSystem(prompt, apiKey = apiKey)
    }

    suspend fun generateCustomProtocol(
        topicOrGoal: String,
        playerRank: String,
        playerLevel: Int,
        apiKey: String = DEFAULT_API_KEY
    ): Result<String> {
        val prompt = """
[REQUEST: CUSTOM PROTOCOL GENERATION]
Goal / Focus Area: $topicOrGoal
Player Level: $playerLevel | Rank: $playerRank

Construct a realistic, high-impact tactical routine for today or this week. Break it down into:
- [OBJECTIVE]
- [3 EXECUTABLE STEPS] (Specific, measurable, time-bounded)
- [EXPECTED EXP & METABOLIC BENEFIT]
- [SYSTEM WARNING] (Common pitfall or distraction to eliminate)
"""
        return chatWithSystem(prompt, apiKey = apiKey)
    }

    private fun executeRequest(model: String, jsonPayload: String, apiKey: String): Result<String> {
        var connection: HttpURLConnection? = null
        return try {
            val endpoint = "$BASE_URL/$model:generateContent?key=$apiKey"
            val url = URL(endpoint)
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15000
                readTimeout = 20000
                doInput = true
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("x-goog-api-key", apiKey)
            }

            OutputStreamWriter(connection.outputStream, "UTF-8").use { writer ->
                writer.write(jsonPayload)
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val responseText = connection.inputStream.bufferedReader().use(BufferedReader::readText)
                val reply = parseGeminiResponse(responseText)
                Result.success(reply)
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.use(BufferedReader::readText) ?: "HTTP $responseCode"
                Log.e(TAG, "Gemini API error ($responseCode): $errorText")
                Result.failure(Exception("System AI Error ($responseCode): $errorText"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network exception connecting to Gemini: ${e.message}", e)
            Result.failure(e)
        } finally {
            connection?.disconnect()
        }
    }

    private fun parseGeminiResponse(jsonString: String): String {
        val root = JSONObject(jsonString)
        val candidates = root.optJSONArray("candidates") ?: return "[SYSTEM ERROR: NO RESPONSE GENERATED]"
        if (candidates.length() == 0) return "[SYSTEM ERROR: EMPTY CANDIDATES]"

        val firstCandidate = candidates.getJSONObject(0)
        val content = firstCandidate.optJSONObject("content") ?: return "[SYSTEM ERROR: NO CONTENT]"
        val parts = content.optJSONArray("parts") ?: return "[SYSTEM ERROR: NO PARTS]"

        val sb = StringBuilder()
        for (i in 0 until parts.length()) {
            val part = parts.getJSONObject(i)
            if (part.has("text")) {
                sb.append(part.getString("text"))
            }
        }
        return sb.toString().trim()
    }
}
