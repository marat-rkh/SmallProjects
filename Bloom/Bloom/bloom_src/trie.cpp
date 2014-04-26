#include "trie.h"

using std::map;
using std::string;
using std::ifstream;

Trie::Trie() : root_(new Node()) {
}

bool Trie::LookUp(string const& word, Node* node) {
    if(word.empty()) {
        if(node->children_.find('$') != node->children_.end()) {
            return true;
        }
        return false;
    }
    Node::MapIter iter = node->children_.find(word.at(0));
    if(iter == node->children_.end()) {
        return false;
    }
    return LookUp(word.substr(1, word.size()), iter->second);
}

void Trie::Insert(string const& word) {
    Node* node = root_;
    size_t pos = 0;
    Node::MapIter iter;
    while(pos < word.size() &&
         (iter = node->children_.find(word.at(pos))) != node->children_.end())
    {
        ++pos;
        node = iter->second;
    }
    while(pos < word.size()) {
        Node* new_node = new Node();
        node->children_.insert(std::make_pair(word.at(pos), new_node));
        node = new_node;
        ++pos;
    }
    node->children_.insert(std::make_pair('$', nullptr));
}

void Trie::DeleteTrie(Node *node) {
    if(node == nullptr) {
        return;
    }
    for(Node::MapIter iter = node->children_.begin(); iter != node->children_.end(); ++iter) {
        DeleteTrie(iter->second);
    }
    delete node;
}
