#include <iostream>

int main(int argc, char const *argv[])
{
    if (argc > 0)
    {
        std::string parametro1 = argv[1];
        std::string parametro2 = argv[2];

        std::cout << parametro1 << parametro2;
    }

    return 0;
}
