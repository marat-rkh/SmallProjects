import matplotlib.pyplot as plt

xs = [x for x in xrange(100, 425, 25)]

#dict 1
results = []
with open("res1") as f:
    results = f.readlines()

ytrie = [results[1] for x in xs]
ymd5 = results[3].split(' ')[:-1];
ymurmur3 = results[5].split(' ')[:-1];
ymurmur1 = results[7].split(' ')[:-1];

trie, = plt.plot(xs, ytrie)
md5, = plt.plot(xs, ymd5)
m3, = plt.plot(xs, ymurmur3)
m1, = plt.plot(xs, ymurmur1)
plt.legend([trie, md5, m3, m1], ["trie", "md5", "murmur3", "murmur1"])
print "sh1"
plt.show()

#dict 2
results = []
with open("res2") as f:
    results = f.readlines()

ytrie = [results[1] for x in xs]
ymd5 = results[3].split(' ')[:-1];
ymurmur3 = results[5].split(' ')[:-1];
ymurmur1 = results[7].split(' ')[:-1];

trie, = plt.plot(xs, ytrie)
md5, = plt.plot(xs, ymd5)
m3, = plt.plot(xs, ymurmur3)
m1, = plt.plot(xs, ymurmur1)
plt.legend([trie, md5, m3, m1], ["trie", "md5", "murmur3", "murmur1"])
print "sh2"
plt.show()

#dict 3
results = []
with open("res3") as f:
    results = f.readlines()

ytrie = [results[1] for x in xs]
ymd5 = results[3].split(' ')[:-1];
ymurmur3 = results[5].split(' ')[:-1];
ymurmur1 = results[7].split(' ')[:-1];

trie, = plt.plot(xs, ytrie)
md5, = plt.plot(xs, ymd5)
m3, = plt.plot(xs, ymurmur3)
m1, = plt.plot(xs, ymurmur1)
plt.legend([trie, md5, m3, m1], ["trie", "md5", "murmur3", "murmur1"])
print "sh3"
plt.show()

#dict 4
results = []
with open("res4") as f:
    results = f.readlines()

ytrie = [results[1] for x in xs]
ymd5 = results[3].split(' ')[:-1];
ymurmur3 = results[5].split(' ')[:-1];
ymurmur1 = results[7].split(' ')[:-1];

trie, = plt.plot(xs, ytrie)
md5, = plt.plot(xs, ymd5)
m3, = plt.plot(xs, ymurmur3)
m1, = plt.plot(xs, ymurmur1)
plt.legend([trie, md5, m3, m1], ["trie", "md5", "murmur3", "murmur1"])
print "sh4"
plt.show()