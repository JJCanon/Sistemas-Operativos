#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
/*
    #include <sndfile.h>
*/

using namespace std;

int main(int argc, char const *argv[])
{
    if (argc = 2) // caso entregen formato especifico
    {
        string parametro1 = argv[1];
        string parametro2 = argv[2];

        cout << "formato: " << parametro1 << " Nombre File " << parametro2;
    }
    else if (argc = 1) // solo proporcionen nombre del archivo
    {
        string parametro1 = argv[1];
        cout << "Nombre File" << parametro1;
    }
    else
    {
        cout << 1;
        return 1;
    }

    return 0;
}

void createThreads(int caso)
{
    string fileName = "hola";
    string outputName = "Adios";
    int numThreads = 4;
    vector<thread> threads;
    mutex fileMutex;

    for (int i = 0; i < numThreads; i++)
    {
        threads.emplace_back(functionThread, ref(fileName), ref(outputName), i, numThreads, ref(fileMutex));
    }

    for (auto &thread : threads)
    {
        thread.join();
    }
}

void functionThread(int threadID)
{
    cout << "Hola desde el hilo" << threadID << "\n";
}