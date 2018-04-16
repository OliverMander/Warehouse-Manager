1) Firstly make sure translation.csv, inital.csv, and traversal_table.csv are in the project file.

2) From the command line pass the filepath of 16orders.txt or the equivalent order file. The order file has the exact same format as the original 16orders.txt. That means: 
	- Placing an order is done in the form Order carModel carModel, ex Order SES White.

	- When a Picker arrives at worker or finishes an order is done in the form: Picker PickerName ready, ex Picker Alice ready.

	- When a Picker is sent to pick a Fascia it is done in the form: Picker PickerName pick pickNumber, ex Picker Alice pick 1.

	- When a Picker is sent to Marshalling it is done in the form: Picker PickerName to Marshalling, ex Picker Alice to Marshalling.

	- When a Sequencer sequences it is done in the form: Sequencer SequencerName sequences, ex Sequencer Sue sequences.

	- When a Loader loads a Truck it is done in the form: Loader LoaderName Loads, ex Loader Lob loads.

	- When a Replenisher restocks a Shelf it is done in the form of: Replenisher ReplenisherName, location, ex Replenisher Ruby replenish A 1 1 2.

2.1) A sample file named 16orders.txt has been supplied to use as example reason.

3) Note the main is in main.java.
	

