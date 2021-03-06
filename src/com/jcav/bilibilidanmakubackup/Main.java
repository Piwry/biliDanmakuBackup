/*
 * Copyright (c) 2019 JCav <825195983@qq.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jcav.bilibilidanmakubackup;

import com.jcav.bilibilidanmakubackup.cidtokenizer.CidTokenizer;
import com.jcav.bilibilidanmakubackup.cidtokenizer.VideoCidTokenizer;
import com.jcav.bilibilidanmakubackup.utilities.CookieUtilities;
import com.jcav.bilibilidanmakubackup.utilities.DanmakuXMLUtilities;
import com.jcav.bilibilidanmakubackup.utilities.IOUtilities;
import com.jcav.bilibilidanmakubackup.utilities.TimeUtilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args){
        Properties stg = new Properties();
        for(String s : args){
            int idx = s.indexOf("=");
            if(idx != -1){
                stg.setProperty(s.substring(0, idx), s.substring(idx + 1));
            }else{
                stg.setProperty(s, "");
            }
        }

        if(stg.containsKey("-timeout")) {
            String timeoutStr = stg.getProperty("-timeout");
            Global.timeout = Integer.valueOf(timeoutStr);
        }

        if(stg.containsKey("-delay")) {
            String delayStr = stg.getProperty("-delay");
            Global.delay = Integer.valueOf(delayStr);
        }

        if(stg.containsKey("-datf")) {
            String datfStr = stg.getProperty("-datf");
            Global.delayAfterTooFrequently = Integer.valueOf(datfStr);
        }

        if(stg.containsKey("-cookie")) {
            String cookiePath = stg.getProperty("-cookie");
            String cookie = CookieUtilities.readCookieFromFile(cookiePath);
            Global.cookie = cookie;
        }

        if(stg.containsKey("-b")){
            if(stg.containsKey("-history")){
                check(stg, "-url", "-st", "-ed", "-cookie");

                if(stg.containsKey("-smart")){
                    BackupHistorySmart backup = new BackupHistorySmart(
                            stg.getProperty("-url"),
                            stg.getProperty("-st"),
                            stg.getProperty("-ed")
                    );
                    backup.start();
                }else {
                    BackupHistory backup = new BackupHistory(
                            stg.getProperty("-url"),
                            stg.getProperty("-st"),
                            stg.getProperty("-ed")
                    );
                    backup.start();
                }
            }else if(stg.containsKey("-single")){
                check(stg, "-url", "-date", "-cookie", "-out");

                BackupSingleDay single = new BackupSingleDay(
                        stg.getProperty("-url"),
                        stg.getProperty("-date"),
                        stg.getProperty("-out")
                );
                single.start();
            }else if(stg.containsKey("-current")){
                check(stg, "-url", "-out");

                BackupCurrent backup = new BackupCurrent(
                        stg.getProperty("-url"),
                        stg.getProperty("-out")
                );
                backup.start();
            }
        }
    }

    private static void check(Properties stg, String ...args){
        for(String s : args){
            if(!stg.containsKey(s)){
                System.out.println("Syntax error.");
                System.exit(0);
            }
        }
    }
}



