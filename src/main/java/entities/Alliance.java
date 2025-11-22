package entities;

import Players.BlackPlayer;
import Players.Player;
import Players.WhitePlayer;
import utils.BoardUtils;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
           return -1; // moving up means subtracting the coordinate index
        }

        @Override
        public boolean isWhite(){
            return true;
        }

        @Override
        public boolean isBlack(){
            return false;
        }
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }
        @Override
        public int getOppositeDirection() {
            return 1;
        }
    },
    BLACK {
        @Override
        public int getDirection(){
            return 1; // moving down means adding the coordinate index
        }
        @Override
        public boolean isWhite(){
            return false;
        }
        @Override
        public boolean isBlack(){
            return true;
        }
        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }
        @Override
        public int getOppositeDirection() {
            return -1;
        }
    };

    // Every alliance must tell their direction
    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract int getOppositeDirection();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
