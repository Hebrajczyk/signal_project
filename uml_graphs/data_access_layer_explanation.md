 Data Access Layer – Explanation

Purpose
This layer is responsible for receiving data from sources such as TCP, WebSocket, File, parsing it into structured objects, and handing it to the system for processing.

Setup (Existing Components)
There were no preexisting components for this part. All elements had to be designed and are yet to be implemented.

Added Components (9 total)

1. DataListener (interface)
Defines the standard `start()` method to begin receiving data. Enables a unified interface for different data sources.

2. TCPDataListener / WebSocketDataListener / FileDataListener (implementations of DataListener)
Each class handles input from a specific external source. They all:
- accept data (raw `String`)
- use a `DataParser` to convert it into structured data
- pass structured data to a `DataSourceAdapter`

3. DataParser (interface)
Defines method `parse(String): PatientInputData` which transforms raw input into an object. 

4. JsonDataParser / CsvDataParser
Specific implementations of `DataParser` that handle JSON and CSV formats.

5. DataSourceAdapter
Receives parsed data and passes it into the main system (e.g., `PatientIdentifier`, `DataStorage`, etc.). 

6. PatientInputData
A simple data class used by parsers to encapsulate incoming information(some new ones form changed patient indentification system) : patient ID, name, date of birth, record type, value, and timestamp.

Key Principles
- All listeners share the same interface (`DataListener`)
- Parsing is done separately with `DataParser` implementations



