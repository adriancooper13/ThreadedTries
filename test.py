import matplotlib
matplotlib.use("agg")

import matplotlib.pyplot as plt
import numpy as np 
import os
import sys

TEST_DIR = "testing/"
TRIES_DIR = "tries/"
GRAPH_DIR = "graphs/"
JSON_FILENAME = "json.txt"
VERSIONS = ["Optimistic", "WaitFree", "FineGrain"]

test_cases = 0
x_ticks = [x for x in range(0, 36, 5)]
y_ticks = [y for y in range(0, 801, 200)]

# Get number of command line arguments (like C).
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

    # Create the full filepath.
    FILEPATH = TEST_DIR + JAVA_FILENAME

    if (max_test is not None and count > max_test) or not os.path.exists(f"{FILEPATH}.java"):
        break

    # Compile java file.
    os.system(f"javac {FILEPATH}.java")

    # If the program did not successfully compile, there will not be a class file.
    if not os.path.exists(f"{FILEPATH}.class"):
        exit()

    # If we make it here, the program compiled successfully.
    # Get the number of test cases and run all of them. Default is 10 testcases.
    test_cases = 10 if argc < 3 or not argv[2].isdigit() else int(argv[2])

    # Create empty dictionary of dictionaries.
    thread_times = {version : {} for version in VERSIONS}

    # For each test case, run the test then read the output file.
    for i in range(test_cases):
        # Run testcase.
        run = f"java {FILEPATH}"
        for version in VERSIONS:
            run += f" {version}"
        
        os.system(run)
        
        for version in VERSIONS:
            # Open the file that the testcase created and read each line.
            ifp = open(version + JSON_FILENAME)
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
                if num_threads in thread_times[version]:
                    thread_times[version][num_threads] = thread_times[version][num_threads] + time
                else:
                    thread_times[version][num_threads] = time
            
            os.system(f"rm {version + JSON_FILENAME}")

    # print("--------------   Averages   --------------")
    # print(thread_times)

    for version in VERSIONS:
        # Get all the averages for thread times.
        for key in thread_times[version]:
            thread_times[version][key] /= test_cases

    # Clean up the mess I made.
    os.system(f"rm {TEST_DIR}*.class")
    os.system(f"rm {TRIES_DIR}*.class")
    os.system(f"rm {TEST_DIR}/all_tests/*.class")

    fig = plt.figure()

    ax = fig.add_subplot(111)

    ax.set_xlabel("Number of Threads")
    ax.set_ylabel("Runtime (milliseconds)")

    ax.set_xticks(x_ticks)
    ax.set_yticks(y_ticks)

    x = np.array([key for key in thread_times[VERSIONS[0]]])
    for version in VERSIONS:
        y = np.array([thread_times[version][key] for key in thread_times[version]])
        ax.plot(x, y, label=version)

    ax.legend()

    test_cases = str(test_cases)
    if not os.path.exists(GRAPH_DIR + test_cases):
        os.mkdir(GRAPH_DIR + test_cases)

    nextfile = len(os.listdir(GRAPH_DIR + test_cases)) + 1
    fig.savefig(f"{GRAPH_DIR}{test_cases}/graph{nextfile}.png")
