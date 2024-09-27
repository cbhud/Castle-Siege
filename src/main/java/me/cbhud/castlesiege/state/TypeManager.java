package me.cbhud.castlesiege.state;
import me.cbhud.castlesiege.CastleSiege;

public class TypeManager{
        private final CastleSiege plugin;
        private Type currentState;

        public TypeManager(CastleSiege plugin) {
            this.plugin = plugin;
            currentState = Type.Normal;
        }

        public void setState(Type newState) {
            this.currentState = newState;
            plugin.getScoreboardManager().updateScoreboardForAll();
        }

        public Type getState() {
            return currentState;
        }


        @Override
        public String toString(){
            return currentState.toString().substring(0, 1).toUpperCase() + currentState.toString().substring(1).toLowerCase();
        }


    }




