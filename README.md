# snaphostlist-maven

***INSTRUCTIONS
Pre-requisites:
		JDK 1.8 and above
		
I. Test Execution

	1. Run the below maven command from the terminal
	    mvn clean test
	
II. PROS

	1. We have Array as the base data structure to provide random access to the elements basedon index.
			1.1 It has time complexity O(1)  when there was huge update on existing elements and finding the elements too.
	2. We have used HashMap to hold the snapshot of the list and snapshot version as the key
			2.1 It has O(2) complexity to retrieve elements from specific snapshot version and then value from the array
	3. Given implementation works well when there are huge updates (value overwrite) over randomly distributed elements
	4. Used TreeMap to store sorted snapshots based on version and it inturm improves drop operations
	
III. CONS

	1. When the elements size grows, array is resized dynamically using copy logic which is time consuming
	2. All the structural modification methods are synchronized and it will be slower in multi-threaded access
