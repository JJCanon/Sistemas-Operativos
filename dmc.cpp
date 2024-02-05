#include <iostream>
#include <thread>
#include <sndfile.h>

using namespace std;

int main(int argc, char const *argv[])
{
    if (argc > 0)
    {
        string parametro1 = argv[1];
        string parametro2 = argv[2];

        cout << parametro1 << " " << parametro2;
    }

    return 0;
}
