#ifndef BLOOMFILTER_H
#define BLOOMFILTER_H

#include <vector>
#include <string>
#include <cmath>

#include "hashes.h"

using std::vector;
using std::string;

const double LN_2 = 0.69314718056;

class BloomFilter {
public:
    BloomFilter(size_t size, size_t insert_count, int mode);
    ~BloomFilter() {
        for(size_t i = 0; i != hfuns_.size(); ++i) {
            delete hfuns_.at(i);
        }
    }
    void Insert(string const& word);
    bool LookUp(string const& word);
private:
    vector<bool> filter_;
    size_t optimal_num_;
    vector<hashes::hashfun*> hfuns_;
};

#endif // BLOOMFILTER_H
