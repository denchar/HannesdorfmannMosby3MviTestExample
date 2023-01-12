package com.example.mviexapplication.View.Data

interface ParticalMainState{
    class Loadind:ParticalMainState
    class GetImageLink(var imageLink:String):ParticalMainState
    class Error(var error:Throwable):ParticalMainState
}