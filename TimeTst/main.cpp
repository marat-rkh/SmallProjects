#include <iostream>
#include <chrono>

using namespace std;
using namespace std::chrono;

int main() {
    auto start = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    auto end = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
    cout << end - start << endl;
    return 0;
}

