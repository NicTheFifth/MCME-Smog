/*
 * This file is part of MCME-pvp.
 * 
 * MCME-pvp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MCME-pvp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MCME-pvp.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package com.mcmiddleearth.mcme.smog;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class DBManager {

    public static void updateFile(HashMap<UUID, Boolean> smogToggle){
        JSONArray playerBoolJSON = new JSONArray();
        smogToggle.forEach( (uuid,bool) -> {
            JSONObject playerJSON = new JSONObject();
            playerJSON.put("UUID", uuid);
            playerJSON.put("Boolean", bool);
            playerBoolJSON.add(playerJSON);
        });

        try (FileWriter file = new FileWriter(SmogPlugin.getPluginDirectory() + SmogPlugin.getFileSep() + "data.json")) {

            file.write(playerBoolJSON.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<UUID, Boolean> loadFile() throws Exception {
        HashMap<UUID, Boolean> smogToggle = new HashMap<>();

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(SmogPlugin.getPluginDirectory() + SmogPlugin.getFileSep() + "data.json"))
        {
            JSONArray playerBoolJSON = (JSONArray) jsonParser.parse(reader);
            playerBoolJSON.forEach(player -> {
                JSONObject playerJSON = (JSONObject) player;
                UUID tempUUID;
                Boolean tempBool;
                tempUUID = (UUID) playerJSON.get("UUID");
                tempBool = (Boolean) playerJSON.get("Boolean");
                smogToggle.put(tempUUID, tempBool);
            });

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (ParseException e) {
            throw new ParseException(e.getErrorType());
        }

        return smogToggle;
    }

}
