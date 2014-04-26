#ifndef HASHES_H
#define HASHES_H

#include <string>
    using std::string;

namespace hashes {

struct hashfun {
    unsigned int seed;

    explicit hashfun(unsigned int seed) : seed(seed) {}
    virtual unsigned int hash(const string &word) const = 0;
    virtual ~hashfun() {}
};

struct murmur: hashfun {
    murmur(unsigned int seed) : hashfun(seed) {}
    unsigned int hash(const string &word) const;
};

struct murmur3: hashfun {
    murmur3(unsigned int seed) : hashfun(seed) {}
    unsigned int hash(const string &word) const;
};

struct md5: hashfun {
    md5(unsigned int seed) : hashfun(seed) {}
    unsigned int hash(const string &word) const;
};

} /* namespace hashes */
#endif // HASHES_H
