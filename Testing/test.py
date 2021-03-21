import matplotlib
matplotlib.use("agg")

import matplotlib.pyplot as plt
import numpy as np 
import os
import sys

JSON_FILENAME = "json.txt"

# Get number of command line arguments (line C).
argv = sys.argv
argc = len(argv)

if argc < 2 or not argv[1].isdigit():
    count = 0
    max_test = None 
else:
    count = int(argv[1]) - 1
    max_test = count + 1

while True:
    # Increase count for next test case.
    count += 1

    # Run test case for everything.
    JAVA_FILENAME = f"TrieTest{count}"
    if (max_test is not None and count > max_test) or not os.path.exists(f"{JAVA_FILENAME}.java"):
        break

    print("Made it")
    # Compile java file.
    os.system(f"javac {JAVA_FILENAME}.java")

    # If the program did not successfully compile, there will not be a class file.
    if not os.path.exists(f"{JAVA_FILENAME}.class"):
        exit()

    # If we make it here, the program compiled successfully.
    # Get the number of test cases and run all of them. Default is 10 testcases.
    TEST_CASES = 10 if argc < 3 or not argv[2].isdigit() else int(argv[2])

    # Create empty dictionary (hashmap).
    thread_times = {}

    # For each test case, run the test then read the output file.
    for i in range(TEST_CASES):
        # Run testcase.
        os.system(f"java {JAVA_FILENAME}")
        
        # Open the file that the testcase created and read each line.
        ifp = open(JSON_FILENAME)
        for line in ifp:
            # Split json key val pairs.
            json_elements = line.split(',')

            # Read first key val pair in json.
            colon = json_elements[0].index(':')
            num_threads = int(json_elements[0][colon+2:])

            # Read second key val pair in json.
            colon = json_elements[1].index(':')
            time = int(json_elements[1][colon+1:])

            # Put time in dictionary.
            if num_threads in thread_times:
                thread_times[num_threads] = thread_times[num_threads] + time
            else:
                thread_times[num_threads] = time
        
        os.system(f"rm {JSON_FILENAME}")
        print()

    print("--------------   Averages   --------------")
    # Get all the averages for thread times.
    for key in thread_times:
        thread_times[key] /= TEST_CASES
        print(f"{key} thread(s): %.2f milliseconds" % thread_times[key])

    # Clean up the mess I made.
    os.system("rm *.class")
    os.system("rm all_tests/*.class")

    x = np.array([key for key in thread_times])
    y = np.array([thread_times[key] for key in thread_times])

    fig = plt.figure()
    plt.xticks(np.arange(0, x[-1] + 1, 5))

    ax = fig.add_subplot(111)
    ax.plot(x, y)

    ax.set_xlabel("Number of Threads")
    ax.set_ylabel("Runtime (milliseconds)")

    fig.savefig("graph.png")
    # plt.scatter(x, y) 