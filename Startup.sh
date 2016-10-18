#!/bin/bash

agentDir=/home/ubuntu/git/CmdExecuter/
cd $agentDir
javac Lab4/HeavyComputing.java
javac Lab4/Server.java
cd Lab4
java Server

