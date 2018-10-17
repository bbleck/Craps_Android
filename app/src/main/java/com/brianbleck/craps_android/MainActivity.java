package com.brianbleck.craps_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.brianbleck.craps_android.model.Game;
import com.brianbleck.craps_android.model.Game.Roll;
import com.brianbleck.craps_android.view.RollAdapter;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private MenuItem next;
  private MenuItem fast;
  private MenuItem pause;
  private MenuItem reset;
  private TextView wins;
  private TextView losses;
  private TextView percentages;
  private boolean running;
  private Game game;
  private ListView rolls;
  private Thread runner;
  private RollAdapter adapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Random rng = new SecureRandom();
    game = new Game(rng);
    wins = findViewById(R.id.wins);
    losses = findViewById(R.id.losses);
    percentages = findViewById(R.id.percentage);
    rolls = findViewById(R.id.rolls);
    adapter = new RollAdapter(this);
    rolls.setAdapter(adapter);
    updateTally(0, 0);
  }

  private void updateRolls(List<Roll> rolls) {
    adapter.clear();
    adapter.addAll(rolls);
  }

  private void updateTally(int wins, int losses) {
    int total = wins + losses;
    double percent = (total!=0) ? (100.0 * wins / total) : 0;
    this.wins.setText(getString(R.string.wins_format, wins));
    this.losses.setText(getString(R.string.losses_format, losses));
    this.percentages.setText(getString(R.string.percent_format, percent));
  }


  //+++++++++++++++++++++  options menu overrides  +++++++++++++++++++++++++++++//
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;//we handle the things we know about, and if it isn't we hand it off to the super class
    switch (item.getItemId()) {
      case R.id.next:
        game.play();
        updateTally(game.getWins(), game.getLosses());
        updateRolls(game.getRolls());

        break;
      case R.id.fast:
        runFast(true);
        break;
      case R.id.pause:
        runFast(false);
        break;
      case R.id.reset:
        game = new Game(new SecureRandom());
        updateTally(game.getWins(), game.getLosses());
        updateRolls(game.getRolls());
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    next = menu.findItem(R.id.next);
    pause = menu.findItem(R.id.pause);
    fast = menu.findItem(R.id.fast);
    reset = menu.findItem(R.id.reset);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    next.setEnabled(!running);
    next.setVisible(!running);
    fast.setVisible(!running);
    fast.setEnabled(!running);
    pause.setEnabled(running);
    pause.setVisible(running);
    reset.setEnabled(!running);
    reset.setVisible(!running);
    
    return true;//tells android to redraw the menu, because I might make things disappear, reappear
  }

//+++++++++++++++++++++++  end of options menu overrides  +++++++++++++++++++++++++//

  private void runFast(boolean start) {
    running = start;
    if (start) {
      invalidateOptionsMenu();
      runner = new Runner();
      runner.start();
    } else {
      runner = null;
    }


  }

  private class Runner extends Thread {

    private static final int TALLY_UPDATE_INTERVAL = 1000;
    private static final int ROLLS_UPDATE_INTERVAL = 10000;

    @Override
    public void run() {
      int count = 0;
      while (running) {
        game.play();
        count++;
        if (count % TALLY_UPDATE_INTERVAL == 0) {
          int wins = game.getWins();
          int losses = game.getLosses();
          runOnUiThread(() -> updateTally(wins, losses));
        }
        if (count % ROLLS_UPDATE_INTERVAL == 0) {
          List<Roll> rolls = game.getRolls();
          runOnUiThread(() -> updateRolls(rolls));
        }
      }
      runOnUiThread(() -> {
        int wins = game.getWins();
        int losses = game.getLosses();
        List<Roll> rolls = game.getRolls();
        updateTally(wins, losses);
        updateRolls(rolls);
        invalidateOptionsMenu();
      });
    }
  }

}