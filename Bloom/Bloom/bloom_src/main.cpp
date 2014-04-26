#include <iostream>
#include <fstream>
#include <chrono>
#include <algorithm>

#include "trie.h"
#include "bloomfilter.h"

using namespace std;
using namespace std::chrono;

long experiment(string const& dict, string const& text, bool silent);
long experimentWithBloom(string const& dict, string const& text, int mode, size_t persentage, bool silent);

void fullTest(string const& dict, string const& text) {
    vector<size_t> pers;
    for(size_t i = 100; i != 425; i += 25) {
        pers.push_back(i);
    }
    long duration = experiment(dict, text, true);
    cout << "No bloom:\n" << duration << endl;

    cout << "Bloom md5 for filter size from 100% to 400% with 25% step:" << endl;
    for (size_t p : pers) {
        vector<long> res;
        for (size_t i = 0; i != 5; ++i) {
            res.push_back(experimentWithBloom(dict, text, 5, p, true));
        }
        std::sort(res.begin(), res.end());
        cout << res.at(2) << ' ';
    }
    cout << endl;

    cout << "Bloom murmur3 for filter size from 100% to 400% with 25% step:" << endl;
    for (size_t p : pers) {
        vector<long> res;
        for (size_t i = 0; i != 5; ++i) {
            res.push_back(experimentWithBloom(dict, text, 3, p, true));
        }
        std::sort(res.begin(), res.end());
        cout << res.at(2) << ' ';
    }
    cout << endl;

    cout << "Bloom murmur1 for filter size from 100% to 400% with 25% step:" << endl;
    for (size_t p : pers) {
        vector<long> res;
        for (size_t i = 0; i != 5; ++i) {
            res.push_back(experimentWithBloom(dict, text, 1, p, true));
        }
        std::sort(res.begin(), res.end());
        cout << res.at(2) << ' ';
    }
    cout << endl;
}

int main(int argc, char** argv) {
    if(argc != 3 && argc != 4 && argc != 5) {
        cout << "Usage: <binfile_name> <dict> <text> [-full | <mode> <size_persentage>]" << endl;
        return 0;
    }
    if(argc == 3) {
        experiment(argv[1], argv[2], false);
    }
    else if(argc == 5) {
        experimentWithBloom(argv[1], argv[2], std::stol(argv[3]), std::stol(argv[4]), false);
    }
    else {
        fullTest(argv[1], argv[2]);
    }
    return 0;
}

long experiment(string const& dict, string const& text, bool silent) {
    ifstream d(dict);
    string words_num_str;
    d >> words_num_str;
    size_t words_num = std::stol(words_num_str);

    Trie trie;
    for(size_t i = 0; i != words_num; ++i) {
        string word;
        d >> word;
        trie.Insert(word);
    }
    ifstream t(text);
    t >> words_num;

    auto start = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    for(size_t i = 0; i != words_num; ++i) {
        string word;
        t >> word;
        bool res = trie.LookUp(word);
        if(!silent) {
            cout << res << endl;
        }
    }
    auto end = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    return end - start;
}

long experimentWithBloom(string const& dict, string const& text, int mode, size_t persentage, bool silent) {
    ifstream d(dict);
    string words_num_str;
    d >> words_num_str;
    size_t words_num = std::stol(words_num_str);
    size_t filter_size = ceil(words_num * (persentage * 1.0 / 100));

    Trie trie;
    BloomFilter bfilter(filter_size, words_num, mode);
    for(size_t i = 0; i != words_num; ++i) {
        string word;
        d >> word;
        trie.Insert(word);
        bfilter.Insert(word);
    }
    ifstream t(text);
    t >> words_num;

    auto start = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    for(size_t i = 0; i != words_num; ++i) {
        string word;
        t >> word;
        if(bfilter.LookUp(word)) {
            bool res = trie.LookUp(word);
            if(!silent) {
                cout << res << endl;
            }
        }
        else {
            if(!silent) {
                cout << false << endl;
            }
        }
    }
    auto end = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    return end - start;
}
