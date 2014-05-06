#include <iostream>
#include <vector>

using namespace std;

const size_t INF = 2 << 20;

struct SimpexTuple {
    vector<size_t> N;
    vector<size_t> B;
    vector<vector<double> > A;
    vector<double> b;
    vector<double> c;
    size_t l;
    size_t e;
};

SimpexTuple Pivot(SimpexTuple& in) {
    vector<double> b1(in.b.size());
    b1[in.e] = in.b[in.l] / in.A[in.l][in.e];
    vector<vector<double> > A1(in.A.size(), vector<double>(in.A[0].size()));
    for(size_t i : in.N) {
        if(i == in.e) { continue; }
        A1[in.e][i] = in.A[in.l][i] / in.A[in.l][in.e];
    }
    A1[in.e][in.l] = 1.0 / in.A[in.l][in.e];
    for(size_t i : in.B) {
        if(i == in.l) { continue; }
        b1[i] = in.b[i] - in.A[i][in.e] * b1[in.e];
        for(size_t j : in.N) {
            if(j == in.e) { continue; }
            A1[i][j] = in.A[i][j] - in.A[i][in.e] * A1[in.e][j];
        }
        A1[i][in.l] = -1 * in.A[i][in.e] * A1[in.e][in.l];
    }
    vector<double> c1(in.c.size());
    for(size_t i : in.N) {
        if(i == in.e) { continue; }
        c1[i] = in.c[i] - in.c[in.e] * A1[in.e][i];
    }
    c1[in.l] = -1 * in.c[in.e] * A1[in.e][in.l];
    vector<size_t> N1;
    for(size_t i : in.N) {
        if(i == in.e) { continue; }
        N1.push_back(i);
    }
    N1.push_back(in.l);
    vector<size_t> B1;
    for(size_t i : in.B) {
        if(i == in.l) { continue; }
        B1.push_back(i);
    }
    B1.push_back(in.e);
    SimpexTuple out = {N1, B1, A1, b1, c1, 0, 0};
    return out;
}

long findPositive(vector<double>& c) {
    for(size_t i = 0; i != c.size(); ++i) {
        if(c[i] > 0) {
            return i;
        }
    }
    return -1;
}

size_t indOfMin(vector<double>& dt) {
    size_t mi = 0;
    for(size_t i = 0; i != dt.size(); ++i) {
        mi = dt[i] < dt[mi] ? i : mi;
    }
    return mi;
}

long contains(vector<size_t>& B, size_t ind) {
    for(size_t i = 0; i != B.size(); ++i) {
        if(B[i] == ind) {
            return i;
        }
    }
    return -1;
}

vector<double> Simplex(SimpexTuple in) {
    long e;
    while((e = findPositive(in.c)) >= 0) {
        vector<double> dt(in.B.size());
        for(size_t i : in.B) {
            if(in.A[i][in.e] > 0) {
                dt[i] = in.b[i] / in.A[i][in.e];
            }
            else {
                dt[i] = INF;
            }
        }
        size_t l = indOfMin(dt);
        if(dt[l] == INF) {
            cout << "bad news\n";
            exit(1);
        }
        else {
            in = Pivot(in);
        }
    }
    vector<double> x(in.N.size());
    for(size_t i = 0; i != in.N.size(); ++i) {
        long ii;
        if((ii = contains(in.B, i)) != -1) {
            x[i] = in.b[i];
        }
        else {
            x[i] = 0;
        }
    }
    return x;
}

int main() {
    cout << "Hello World!" << endl;
    return 0;
}
