# ConcurrentProgrammingProblems1
University concurrent programming problems

Problems:

Question 6
Implement a program to sort n integers using a pipeline of n components (note that the value of n is known in advance). Each component should receive a stream of values on its left channel, keep the largest, and pass the rest to the next process, via its right channel. Each process should hold at most two values at any time: the most recently received value and the largest seen so far. Think about how the end of the input stream should be signalled. At the end, the sorted values should be output on the right-most channel.
Implement a testing rig for the sorting network. How many messages are sent in total?
2
What is the execution time of the program in terms of the number of sequentially- ordered messages? In other words, what is the length of the longest totally ordered chain ofmessagesm1 ≺m2 ≺...≺mk?
Question 7
Write a concurrent program to multiply two large n by n matrices a and b together, storing the result in matrix c. You should use the bag-of-tasks pattern. You should consider what a suitable “task” should be. Optional: carry out some experiments to assess different sizes of tasks.
