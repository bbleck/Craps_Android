package com.brianbleck.craps_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.brianbleck.craps_android.model.Game;
import java.security.SecureRandom;
import java.util.Random;
import org.w3c.dom.Text;

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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Random rng = new SecureRandom();
    game = new Game(rng);
    wins = findViewById(R.id.wins);
    losses = findViewById(R.id.losses);
    percentages = findViewById(R.id.percentage);
  }

  private void updateTally(){
    int wins = game.getWins();
    int losses = game.getLosses();
    double percent = 100.0 * wins / (wins+losses);
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
        updateTally();
        //todo: play one game
        break;
      case R.id.fast:
        runFast(true);
        break;
      case R.id.pause:
        runFast(false);
        break;
      case R.id.reset:
        //todo: reset game tallies
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
    //todo: enable/disable individual item references
    return true;//tells android to redraw the menu, because I might make things disappear, reappear
  }

//+++++++++++++++++++++++  end of options menu overrides  +++++++++++++++++++++++++//

  private void runFast(boolean start){
    running = start;
    //todo: start a thread to play
    invalidateOptionsMenu();
  }



}