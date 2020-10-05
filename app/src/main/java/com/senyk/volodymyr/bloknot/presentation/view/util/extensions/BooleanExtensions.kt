package com.senyk.volodymyr.bloknot.presentation.view.util.extensions

inline val Boolean?.int get() = if (this == true) 1 else 0
