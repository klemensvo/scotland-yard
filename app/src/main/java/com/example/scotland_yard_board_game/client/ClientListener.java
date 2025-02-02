package com.example.scotland_yard_board_game.client;

import static android.content.ContentValues.TAG;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.example.scotland_yard_board_game.common.messages.GameStart;
import com.example.scotland_yard_board_game.common.messages.fromserver.ColorTaken;
import com.example.scotland_yard_board_game.common.messages.fromserver.DetectivesWon;
import com.example.scotland_yard_board_game.common.messages.fromserver.EndTurn;
import com.example.scotland_yard_board_game.common.messages.fromserver.InvalidMove;
import com.example.scotland_yard_board_game.common.messages.fromserver.TravelLog;
import com.example.scotland_yard_board_game.common.messages.fromserver.MisterXWon;
import com.example.scotland_yard_board_game.common.messages.fromserver.PlayerConnected;
import com.example.scotland_yard_board_game.common.messages.fromserver.PlayerList;
import com.example.scotland_yard_board_game.common.messages.fromserver.ServerFull;
import com.example.scotland_yard_board_game.common.messages.fromserver.StartTurn;

public class ClientListener extends Listener {
    private ClientData clientData;

    public ClientListener(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public void received(Connection connection, Object object) {

        // keep KeepAlive messages from spamming the log
        if (!(object instanceof FrameworkMessage.KeepAlive)) {
            Log.info("Message received from Server " + connection.toString() +
                    " at endpoint " + connection.getRemoteAddressTCP().toString() +
                    "; message class = " + object.getClass().getTypeName());
        }

        if (object instanceof ServerFull) {
            clientData.serverFull();
        } else if (object instanceof ColorTaken) {
            clientData.colourTaken();
        } else if (object instanceof InvalidMove) {
            clientData.invalidMove();
        } else if (object instanceof TravelLog) {
            TravelLog jtable = (TravelLog) object;
            clientData.updateJourneyTable(jtable);
        } else if (object instanceof PlayerConnected) {
            Log.debug(TAG, "Connection successfull");
        } else if (object instanceof PlayerList) {
            PlayerList list = (PlayerList) object;
            clientData.updatePlayers(list);
        } else if (object instanceof GameStart) {
            clientData.gameStarted();
        } else if (object instanceof StartTurn) {
            clientData.startTurn();
        } else if (object instanceof EndTurn) {
            clientData.endTurn();
        } else if (object instanceof DetectivesWon) {
            clientData.won(0);
        } else if (object instanceof MisterXWon) {
            clientData.won(1);
        }

    }

    @Override
    public void disconnected(Connection connection) {
        Log.info("Disconnected");
        clientData.disconnectClient();
        // TODO: 6/9/2022 handle disconnect
    }
}


