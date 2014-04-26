#include "bloomfilter.h"

BloomFilter::BloomFilter(size_t size, size_t insert_count, int mode)
    : filter_(size, 0)
    , optimal_num_(ceil(size * 1.0 / insert_count * LN_2))
    , hfuns_(optimal_num_, nullptr)
{
    switch (mode) {
    case 1:
        for(size_t i = 0; i != optimal_num_; ++i) {
            hfuns_.at(i) = new hashes::murmur(rand());
        }
        break;
    case 3:
        for(size_t i = 0; i != optimal_num_; ++i) {
            hfuns_.at(i) = new hashes::murmur3(rand());
        }
        break;
    case 5:
        for(size_t i = 0; i != optimal_num_; ++i) {
            hfuns_.at(i) = new hashes::md5(rand());
        }
        break;
    }
}

void BloomFilter::Insert(string const& word) {
    for(size_t i = 0; i != hfuns_.size(); ++i) {
        size_t pos = hfuns_.at(i)->hash(word) % filter_.size();
        filter_.at(pos) = true;
    }
}

bool BloomFilter::LookUp(const string &word) {
    for(size_t i = 0; i != hfuns_.size(); ++i) {
        size_t pos = hfuns_.at(i)->hash(word) % filter_.size();
        if(!filter_.at(pos)) {
            return false;
        }
    }
    return true;
}
