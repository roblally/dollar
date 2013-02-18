package com.robertlally.dollar;

interface Pipeline<T> {
    Option<T> pull();
}
