TEMPLATE = app
CONFIG += console
CONFIG -= app_bundle
CONFIG -= qt

CONFIG += c++11

SOURCES += main.cpp \
    trie.cpp \
    bloomfilter.cpp \
    murmurhash1.cpp \
    murmurhash3.cpp \
    hashes.cpp

HEADERS += \
    trie.h \
    bloomfilter.h \
    murmurhash1.h \
    murmurhash3.h \
    hashes.h

LIBS += -L/usr/lib -lssl -lcrypto
