from numpy import *

def plotPoint(filename):
    fr = open(filename)
    yes_array = []
    no_array = []
    cnt = 0
    import matplotlib.pyplot as plt
    fig = plt.figure()
    ax = fig.add_subplot(111)
    for line in fr.readlines():
        line_array = []
        cur_line = line.strip().split('\t')
        if cnt == 0:
            plt.xlabel(cur_line[0])
            plt.ylabel(cur_line[1])
            plt.title("Tail Recursion Test in JDK")
        elif len(cur_line) < 2:
            cnt += 1
            continue  # ignore the exception message
        else:
            for i in range(2):
                line_array.append(float(cur_line[i]))
            if cnt%2 == 1:  # for line without recursion
                no_array.append(line_array)
            else:  # for line with recursion
                yes_array.append(line_array)
        cnt = cnt + 1
    yes_mat = mat(yes_array)
    no_mat = mat(no_array)
    ax.plot(yes_mat[:, 0], yes_mat[:, 1], 'b.-', label='ParserWithRecursion')
    ax.plot(no_mat[:, 0], no_mat[:, 1], 'r*--', label='ParserWithoutRecursion')
    plt.legend(loc="upper left", shadow=True, fancybox=True)
    plt.show()




