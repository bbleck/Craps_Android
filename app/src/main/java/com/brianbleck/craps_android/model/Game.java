package com.brianbleck.craps_android.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {

  public int getWins() {
    return wins;
  }

  public int getLosses() {
    return losses;
  }

  private State state = State.COME_OUT;
  private int point;
  private Random rng;
  private List<Roll> rolls;
  private int wins;
  private int losses;
  private final Object lock = new Object();

  public Game(Random rng) {
    this.rng = rng;
    rolls = new LinkedList<>();
    wins = 0;
    losses = 0;
  }

  public static class Roll {

    private final int[] dice;
    private final State state;

    public Roll(int[] dice, State state) {
      this.dice = Arrays.copyOf(dice, 2);
      this.state = state;
    }

    public int[] getDice() {
      return Arrays.copyOf(dice, 2);
    }

    public State getState() {
      return state;
    }

    @Override
    public String toString() {
      return String.format("%s %s%n", Arrays.toString(dice), state);
    }
  }

  private State rollDice() {
    int[] dice = {
        rng.nextInt(6) + 1,
        rng.nextInt(6) + 1
    };
    int total = dice[0] + dice[1];
    State state = this.state.roll(total, point);
    if (this.state == State.COME_OUT && state == State.POINT) {
      point = total;
    }
    this.state = state;
    synchronized (lock) {
      rolls.add(new Roll(dice, state));
    }
    return state;
  }

  public void reset(){
    state = State.COME_OUT;
    synchronized (lock) {
      rolls.clear();
    }
    point = 0;
  }

  public State play() {
    reset();
    while (state != State.WIN && state != State.LOSS) {
      rollDice();
    }
    if(state == State.WIN){
      wins++;
    }else{
      losses++;
    }
    return state;
  }

  public State getState() {
    return state;
  }

  public List<Roll> getRolls() {
    synchronized (lock) {
      return new LinkedList<>(rolls);
    }
  }

  public enum State {//enums nested in another class are always static
    COME_OUT {
      @Override
      public State roll(int total, int point) {
        switch (total) {
          case 2:
          case 3:
          case 12:
            return LOSS;
          case 7:
          case 11:
            return WIN;
          default:
            return POINT;
        }
      }
    },
    WIN,
    LOSS,
    POINT {
      @Override
      public State roll(int total, int point) {
        if (total == point) {
          return WIN;
        } else if (total == 7) {
          return LOSS;
        } else {
          return this;
        }
      }
    };

    public State roll(int total, int point) {
      return this;
    }
  }

}
