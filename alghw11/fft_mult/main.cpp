#include <iostream>
#include <complex>
#include <vector>
#include <cmath>
#include <cstdio>
#include <cstdlib>

using namespace std;

typedef complex<double> cod;

void NormalizeLength(string& a, string& b) {
    int sa = a.size();
    int sb = b.size();
    int m = sa > sb ? sa : sb;
    m *= 2;
    int p2 = 2;
    while (p2 < m) { p2 *= 2; }
    string zeros = "";
    while (sa < p2) {
        zeros += "0";
        sa += 1;
    };
    a = zeros + a;
    zeros = "";
    while (sb < p2) {
        zeros += "0";
        sb += 1;
    };
    b = zeros + b;
}

void FFT (vector<cod>& a, bool invert) {
    int n = (int) a.size();
    if (n == 1)  return;

    vector<cod> a0 (n/2),  a1 (n/2);
    for (int i=0, j=0; i<n; i+=2, ++j) {
        a0[j] = a[i];
        a1[j] = a[i+1];
    }
    FFT (a0, invert);
    FFT (a1, invert);

    double ang = 2*M_PI/n * (invert ? -1 : 1);
    cod w (1),  wn (cos(ang), sin(ang));
    for (int i=0; i<n/2; ++i) {
        a[i] = a0[i] + w * a1[i];
        a[i+n/2] = a0[i] - w * a1[i];
        if (invert)
            a[i] /= 2,  a[i+n/2] /= 2;
        w *= wn;
    }
}

void Mult(const vector<int> & a, const vector<int> & b, vector<int> & res) {
    vector<cod> fa (a.begin(), a.end());
    vector<cod> fb (b.begin(), b.end());
    size_t n = 1;
    while (n < max (a.size(), b.size()))  n <<= 1;
    n <<= 1;
    fa.resize (n);
    fb.resize (n);

    FFT(fa, false);
    FFT(fb, false);
    for (size_t i=0; i<n; ++i) {
        fa[i] *= fb[i];
    }
    res.resize(n);
    FFT(fa, true);

    for(size_t i = 0; i != fa.size(); ++i) {
        res[i] = fa[i].real();
    }

    res.resize (n);
    for (size_t i=0; i<n; ++i)
        res[i] = int (fa[i].real() + 0.5);
}

void NormalizeResult(vector<int> & res) {
    int carry = 0;
    for (size_t i = 0; i < res.size(); ++i) {
        res[i] += carry;
        carry = res[i] / 10;
        res[i] %= 10;
    }
}

//#define DEBUG

int main() {
#ifdef DEBUG
    std::freopen("../fft_mult/test0", "r", stdin);
#endif
    size_t n;
    cin >> n;
    for(size_t i = 0; i != n; ++i) {
        vector<int> a;
        string s1, s2;
        cin >> s1 >> s2;
        for(long j = s1.size() - 1; j >= 0; --j) {
            char ch = s1[j];
            a.push_back(atoi(&ch));
        }
        vector<int> b;
        for(long j = s2.size() - 1; j >= 0; --j) {
            char ch = s2[j];
            b.push_back(atoi(&ch));
        }
        vector<int> res;
        Mult(a, b, res);
        NormalizeResult(res);
        long j = res.size() - 1;
        while(res[j] == 0 && j != 0) { --j; }
        while(j >= 0) {
            cout << res[j];
            --j;
        }
        cout << endl;
    }
    return 0;
}
