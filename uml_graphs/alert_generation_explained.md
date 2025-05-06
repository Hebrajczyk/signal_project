Alert Generation System UML – explanation

The Alert Generation System is designed to evaluate patient data in real time and generate alerts when conditions are met. Each patient can have different alert thresholds. This allows flexibility in defining various alerting behaviors without chaning the core logic.

The central class is AlertGenerator, which is responsible for checking patient records against assigned rules. It uses a Map<patientId, List<AlertRule>> to assign specific rules per patient. These rules implement the AlertRule interface, which defines the method check, returning a boolean. One implementation is ThresholdRule.

When a rule is triggered, the generator creates an Alert object, which includes the patient ID, condition description, and timestamp. The alert is then forwarded to an AlertManager, a component responsible for dispatching or logging alerts. 

I tried to make this system as OOP as possible to my extent, for that reason I added additional components which I will further implement (I suppose the methods with blank bodies are to be implemented later since there is no info on it in week1 or week2 tut), here is a list:

What exists:

Patient, PatientRecord, DataStorage, Alert – already implemented

AlertGenerator – partially implemented (needs rule logic)

What needs to be created:

AlertRule (interface for strategy)

ThresholdRule (example strategy implementation)

AlertManager (handles alert dispatch)


