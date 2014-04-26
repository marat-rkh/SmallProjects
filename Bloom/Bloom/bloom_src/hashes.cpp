#include "hashes.h"
#include "murmurhash1.h"
#include "murmurhash3.h"
#include <openssl/md5.h>
#include <cstring>

namespace hashes {

unsigned int murmur::hash(const string &word) const
{
    return MurmurHash1Aligned(word.c_str(), word.size(), seed);
}

unsigned int murmur3::hash(const string &word) const
{
    return MurmurHash3_x86_32(word.c_str(), word.size(), seed);
}

unsigned int md5::hash(const string &word) const
{
    unsigned char data[16];
    MD5((const unsigned char*)word.c_str(), word.size(), data);
    unsigned int n[4];
    memcpy(&n[0], &data[0], 4);
    memcpy(&n[1], &data[4], 4);
    memcpy(&n[2], &data[8], 4);
    memcpy(&n[3], &data[12], 4);
    unsigned int result = n[0] ^ n[1] ^ n[2] ^ n[3] ^ seed;
    return result;
}

} /* namespace hashes */
