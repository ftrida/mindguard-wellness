# MindGuard AI Stress Likelihood Engine Blueprint
## Enterprise Stress Likelihood Estimation, Multi-Factor Fusion, and Explainable AI (XAI) System

This document details the production-ready architecture for the **Stress Likelihood Estimation Engine** (SLEE) of MindGuard AI. The engine evaluates behavioral data from the Digital Lifestyle Twin and the Behavior Analysis Engine to estimate the likelihood of elevated user stress, generating natural-language explanations without making clinical claims.

---

## 1. Stress Likelihood Engine Architecture

The Stress Likelihood Engine acts as the final analytical layer in the behavioral data pipeline, running after the Behavior Analysis Engine has evaluated daily habits.

```
       [Behavior Analysis Engine (BAE)]
                       │ (Daily Deviations, Consistency Scores)
                       ▼
  ┌────────────────────────────────────────────────────────┐
  │ 1. Data Ingestion & Scaling Layer                      │
  │    - Extract Z-scores, mood averages, & sentiment      │
  └────────────────────────┬───────────────────────────────┘
                           │ Prepared Input Vector
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 2. Multi-Factor Fusion Processor                       │
  │    - Apply weighted fusion to risk & protective inputs │
  └────────────────────────┬───────────────────────────────┘
                           │ Likelihood Index (0.0 to 1.0)
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 3. Classification & Confidence Engine                  │
  │    - Map to category & compute confidence score        │
  └────────────────────────┬───────────────────────────────┘
                           │ Categorized Stress State
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 4. Explainable AI & Recommendation Engine              │
  │    - Generate dynamic text rationales & wellness tips  │
  └────────────────────────┬───────────────────────────────┘
                           │ Output Report Object
                           ▼
  ┌────────────────────────┴───────────────────────────────┐
  │ 5. Downstream Consumers                                │
  │    - DB Storage  ──►  Notification  ──►  AI Coach      │
  └────────────────────────────────────────────────────────┘
```

---

## 2. Complete Processing Pipeline

The SLEE executes a structured processing pipeline for each user on a nightly schedule or upon manual dashboard refreshes:
1.  **Ingestion:** Reads the user's daily behavior analysis report, extracting metric deviations ($Z$-scores), mood averages, and journal sentiment scores.
2.  **Normalization:** Scales all incoming metrics into a normalized range of `[-1.0, 1.0]`, where negative values represent poor habits and positive values represent healthy habits.
3.  **Sensor/Metric Fusion:** Runs the multi-factor weighted fusion formula to compute a raw stress likelihood index (0.0 to 100.0).
4.  **Classification:** Maps the raw score to a risk category (e.g. 'moderate', 'elevated').
5.  **Explainability Analysis:** Identifies the top risk and protective factors, and uses text templates to compile the final explainable report.
6.  **Persistence:** Saves the results to the database, updates the cache, and forwards the report to the AI Coach.

---

## 3. Input Feature Analysis

The engine processes a structured input vector containing the following metrics:
*   `sleep_duration_deviation`: $Z$-score of daily sleep duration.
*   `sleep_consistency`: Calculated sleep consistency rating (0 to 100).
*   `screen_time_deviation`: $Z$-score of daily device usage.
*   `unlock_count_deviation`: $Z$-score of daily device unlocks.
*   `exercise_frequency`: Active exercise minutes.
*   `meditation_consistency`: Meditation completion rate.
*   `focus_duration_deviation`: $Z$-score of focused study/work minutes.
*   `mood_average`: Average daily mood rating (1 to 5).
*   `journal_sentiment`: Calculated sentiment score (`-1.0` to `+1.0`).

---

## 4 & 5. Risk vs. Protective Factors

```
       [Protective Factors]                     [Risk Factors]
     - Regular Exercise                       - Sleep Deprivation
     - Consistent Sleep                       - Elevated Screen Time
     - Daily Meditation                       - Frequent Device Unlocks
     - Stable Mood Averages                   - Declining Mood Trends
     - Positive Journal Sentiment             - Negative Journal Sentiment
               │                                        │
               ▼ (Reduces Score)                        ▼ (Increases Score)
  ┌────────────────────────────────────────────────────────────────────────┐
  │                        Stress Likelihood Index                         │
  └────────────────────────────────────────────────────────────────────────┘
```

---

## 6. Multi-Factor Fusion Strategy

To prevent single anomalies (such as one night of poor sleep) from triggering stress alerts, we evaluate multiple behavioral signals together. The raw stress index ($S$) is calculated using a weighted combination of risk and protective factors:

$$S = \text{Clamp}\left( \sum_{i=1}^{n} (w_i \cdot R_i) - \sum_{j=1}^{m} (v_j \cdot P_j) , 0.0 , 100.0 \right)$$

*   **$R_i$**: Calculated values for risk factors (such as sleep deficits or elevated screen time).
*   **$P_j$**: Calculated values for protective factors (such as exercise duration or meditation completion).
*   **$w_i, v_j$**: Dynamic weights configured in the application settings:
    *   Sleep Deficit Weight: `25%`
    *   Mood Trend Deficit Weight: `20%`
    *   Screen Time Increase Weight: `15%`
    *   Journal Sentiment Deficit Weight: `15%`
    *   Meditation Completion Weight: `-15%`
    *   Exercise Frequency Weight: `-10%`

---

## 7. Explainable AI (XAI) Workflow

Every stress estimation result includes a structured explanation that details the primary behavioral factors driving the score.

```
 [Raw Stress Index = 72% (Elevated)] ──► [SLEE Explainability Engine]
                                                   │
                                        Evaluate contributing weights
                                                   │
                                                   ▼
                     [Generated Explainable AI Report]
                     Observation: Stress likelihood is Elevated.
                     Reason: Sleep duration dropped while device unlocks doubled.
                     Evidence: Sleep was 2h below baseline; unlocks rose by 90.
                     Confidence: High (88%)
                     Actionable Tip: Maintain a consistent bedtime routine.
```

### Positive Explanations:
When the user's stress likelihood decreases, the engine generates positive reinforcement rationales:
*   *"Your recent routine indicates a low stress likelihood. Meditation consistency improved by 35% and mood averages remained stable."*

---

## 8. Confidence Scoring Strategy

The engine calculates a confidence score (low, medium, high, very high) for each stress estimation, reflecting the completeness of the input data:

$$Confidence = w_1 \cdot \text{Data\_Completeness} + w_2 \cdot \text{Historical\_Depth}$$

```
  Confidence Category      Condition                              UX Representation
  ┌───────────────────────┬──────────────────────────────────────┬──────────────────┐
  │ Low                   │ Baseline < 7 days OR logs < 50%      │ Dotted badge     │
  ├───────────────────────┼──────────────────────────────────────┼──────────────────┤
  │ Medium                │ Baseline 7-14 days OR logs 50-80%    │ Thin border      │
  ├───────────────────────┼──────────────────────────────────────┼──────────────────┤
  │ High                  │ Baseline 14-30 days AND logs 80-90%  │ Solid badge      │
  ├───────────────────────┼──────────────────────────────────────┼──────────────────┤
  │ Very High             │ Baseline 30+ days AND logs 90-100%   │ Glow effect badge│
  └───────────────────────┴──────────────────────────────────────┴──────────────────┘
```

---

## 9. Historical Comparison Workflow

To show how the user's stress trends are changing, the engine compares today's stress likelihood ($S_{\text{today}}$) against their historical averages:
*   **7-Day Short-Term Trend:** Evaluates changes over the past week:
    $$\Delta S_{7d} = S_{\text{today}} - \overline{S}_{7d}$$
*   **30-Day Mid-Term Trend:** Evaluates changes over the past month.
*   **Trend Classification:**
    *   $\Delta S > 15\%$: *Significantly Increased*
    *   $5\% < \Delta S \le 15\%$: *Slightly Increased*
    *   $-5\% \le \Delta S \le 5\%$: *Stable*
    *   $\Delta S < -5\%$: *Improved*

---

## 10. Recommendation Generation Strategy

The engine generates personalized, non-medical wellness recommendations based on the primary contributors to the stress score:

```
  Primary Contributing Anomaly              Generated Recommendation
  ┌────────────────────────────────────────┬────────────────────────────────────────┐
  │ Sleep deficit > 1.5 Standard Devs      │ Avoid screen usage 30m before bedtime. │
  ├────────────────────────────────────────┼────────────────────────────────────────┤
  │ Meditation frequency < 50% of baseline │ Block 5 minutes for guided breathing.  │
  ├────────────────────────────────────────┼────────────────────────────────────────┤
  │ Focus time drops & unlocks double      │ Start a Pomodoro block; lock app use.  │
  └────────────────────────────────────────┴────────────────────────────────────────┘
```

---

## 11. Notification Strategy

The Notification Engine is configured to protect user attention, avoiding alarmist language and unnecessary alerts.
*   **No Alerts for Normal Variation:** Fluctuations within standard limits do not trigger push notifications.
*   **Gentle Notification Tone:** Alerts use encouraging, supportive language:
    *   *System Notification:* *"We noticed changes in your recent routine. Consider reviewing today's wellness insights."*
*   **Quiet Hours:** Push notifications are blocked during the user's configured quiet hours.

---

## 12. Ethical AI Guidelines

MindGuard AI enforces strict guidelines to ensure the system is positioned as a wellness tool, not a medical device:
1.  **No Clinical Terminology:** The system never references medical terms such as "depression", "anxiety disorder", or "clinical burnout".
2.  **No Clinical Guarantees:** Estimates are framed as probabilities based on habit changes: *"Your recent routine suggests a higher stress likelihood"*, rather than *"You are stressed"*.
3.  **Crisis Interventions:** If a user's journal entries or chat messages contain indicators of self-harm, the system suspends wellness recommendations and displays direct helpline contacts.

---

## 13. Database Integration

The engine writes stress estimation records to the MySQL database:
*   `stress_estimations`: Stores the raw score, risk category, confidence rating, and generated explanation.
*   `stress_factors` (Join Table): Stores contributing factors and their calculated weights for each daily analysis.

---

## 14. FastAPI Integration

The Stress Estimation Engine exposes the following endpoints:
*   `GET /api/v1/stress/today`: Returns the user's stress likelihood report for today.
*   `GET /api/v1/stress/history?days=30`: Returns daily stress likelihood values over the requested range.
*   `POST /api/v1/stress/calculate`: Triggers the stress calculation task (restricted to background workers).

---

## 17. Future Machine Learning Architecture

The engine is designed to allow component upgrades without database redesign:

```
                   ┌───────────────────────────────┐
                   │    Input Feature Vector       │
                   └───────────────┬───────────────┘
                                   │
         ┌─────────────────────────┼─────────────────────────┐
         ▼ (Current Pipeline)      ▼ (Future Pipeline)       ▼ (Future Pipeline)
  ┌──────────────┐          ┌──────────────┐          ┌──────────────┐
  │ Weighted     │          │ ONNX Runtime │          │ LLM Prompt   │
  │ Heuristic    │          │ Random Forest│          │ Injector     │
  └──────────────┘          └──────────────┘          └──────────────┘
```

1.  **ONNX Integration:** The weighted heuristic models can be replaced with machine learning models (e.g. Random Forest classifiers) exported to ONNX format.
2.  **Federated Learning Support:** The engine can be configured to train classification layers locally on client devices, keeping sensitive user logs private.
3.  **LLM Prompt Injection:** Exposes structured JSON summaries of the stress report, which can be injected directly into LLM prompts for the AI Coach.

---

## 18. Scalability Plan

*   **Asynchronous Calculations:** Daily stress calculations are processed asynchronously using **Celery** workers.
*   **Caching:** Calculated stress reports are cached in Redis, enabling fast retrieval for client dashboards.
*   **Database Indexes:** We index the `stress_estimations` table on `(user_id, log_date)`, keeping query times under 50ms for historical reports.

---

## 19. Development Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: Math models & multi-factor fusion algorithms  │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: Stress DB tables & calculation pipelines      │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: Explainability templates & recommendation math│
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 4: Integration testing and API routing           │
  └───────────────────────────┬────────────────────────────┘
```

1.  **Phase 1 (Foundations):** Write helper classes for the multi-factor weighted fusion and category mapping equations, verifying accuracy against simulated datasets.
2.  **Phase 2 (Database Integration):** Connect the calculation pipeline to the database, and configure indexes for fast history lookups.
3.  **Phase 3 (Explainability & Tips):** Build the explainability report generator and implement the recommendation engine.
4.  **Phase 4 (API & Testing):** Expose endpoint routes, run integration tests, and configure background workers.

---

## 20. Research & Future Enhancements

*   **Heart Rate Variability (HRV) Integration:** Future updates can integrate HRV metrics from smartwatches (such as Apple Watch or Fitbit) to supplement lifestyle logs.
*   **Cognitive Behavioral Therapy (CBT) Matching:** Connect the recommendation engine to structured CBT exercises (e.g. cognitive reframing or progressive muscle relaxation) tailored to the detected deviations.
