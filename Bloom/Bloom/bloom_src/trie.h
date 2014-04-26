#ifndef TRIE_H
#define TRIE_H

#include <map>
#include <string>
#include <fstream>

using std::map;
using std::string;
using std::ifstream;

struct Node {
    typedef map<char, Node*>::const_iterator MapIter;
    map<char, Node*> children_;
};

class Trie {
public:
    Trie();
    ~Trie() { DeleteTrie(root_); }
    bool LookUp(string const& word) { return LookUp(word, root_); }
    void Insert(string const& word);
private:
    bool LookUp(string const& word, Node* node);
    void DeleteTrie(Node* node);

    Node* root_;
};

#endif // TRIE_H
