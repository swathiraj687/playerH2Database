/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;

import java.util.*;

import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlayerH2Service implements PlayerRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getAllPlayers() {
        List<Player> playerList = db.query("SELECT * FROM cricketteam", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("SELECT * FROM cricketteam WHERE id =?", new PlayerRowMapper(), playerId);
        return player;
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("INSERT INTO cricketteam(playerName, jerseyNumber, role) VALUES (?, ?, ?)", player.getPlayerName(),player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject("SELECT * FROM cricketteam WHERE playerName=? and jerseyNumber=? and role=?",
            new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savedPlayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("UPDATE cricketteam SET playerName = ? WHERE id = ?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("UPDATE cricketteam SET jerseyNumber = ? WHERE id = ?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            db.update("UPDATE cricketteam SET role = ? WHERE id = ?", player.getRole(), playerId);
        }

        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        db.update("DELETE FROM cricketteam WHERE id=?", playerId);
    }

}
