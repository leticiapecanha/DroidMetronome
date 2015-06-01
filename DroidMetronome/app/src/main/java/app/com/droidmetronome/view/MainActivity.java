package app.com.droidmetronome.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import app.com.droidmetronome.R;
import app.com.droidmetronome.control.Executer;
import app.com.droidmetronome.control.FrontConversor;


public class MainActivity extends ActionBarActivity {
    private Executer executer;
    private boolean inExecution;
    private int idSom = 1;
    private int idBit8 = 1;
    private int idHihats = 2;
    private int idKickClap = 3;
    private int idRimshot = 4;
    private int idBeep = 5;

    private NumberPicker npBPM;
    private NumberPicker npQntBatidas;
    private NumberPicker npValorBase;
    private NumberPicker npTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mountInterface();

        executer =  new Executer();
        inExecution = false;
    }

    /**
     * Retorna o id do som escolhido pelo usuário
     */
    public int getIdSom() {
        return idSom;
    }

    /**
     * Seta o id do som escolhido pelo usuário como Bit8.
     * @param view
     */
    public void setIdBit8(View view) {
        this.idSom = idBit8;
    }

    /**
     * Seta o id do som escolhido pelo usuário como idHihats.
     * @param view
     */
    public void setIdHihats(View view) {
        this.idSom = idHihats;
    }

    /**
     * Seta o id do som escolhido pelo usuário como kickclap.
     * @param view
     */
    public void setIdKickClap(View view) {
        this.idSom = idKickClap;
    }

    /**
     * Seta o id do som escolhido pelo usuário como Rimshot.
     * @param view
     */
    public void setIdRimshot(View view) {
        this.idSom = idRimshot;
    }

    /**
     * Seta o id do som escolhido pelo usuário como Beep.
     * @param view
     */
    public void setIdBeep(View view) {
        this.idSom = idBeep;
    }


    private void mountInterface(){
        npBPM = (NumberPicker) findViewById(R.id.bpm);
        npBPM.setMinValue(10);
        npBPM.setMaxValue(300);
        npBPM.setValue(120); //Padrão

        npTimer = (NumberPicker) findViewById(R.id.timer);
        npTimer.setMinValue(1);
        npTimer.setMaxValue(15);
        npTimer.setValue(1); //Padrão

        npQntBatidas = (NumberPicker) findViewById(R.id.qntBatidas);
        npQntBatidas.setMinValue(1);
        npQntBatidas.setMaxValue(16);
        npQntBatidas.setValue(4); // Padrão

        npValorBase = (NumberPicker) findViewById(R.id.valorBase);
        npValorBase.setMinValue(1);

        npValorBase.setMaxValue(32);
        npValorBase.setValue(4);


    }

    /**
     * Prepara e executa o metronomo.
     * @param view
     */
    public void executar(View view){
        if(!inExecution) {

            FrontConversor conversor = new FrontConversor();
            conversor.setTempoMinutos(npTimer.getValue());
            conversor.setFiguraRitmica(1);
            conversor.setFrequenciaBPM(npBPM.getValue());
            conversor.setQuantidadeBatidas(npQntBatidas.getValue());
            conversor.createSomById(getIdSom(),this);

            executer.preExecuter(conversor); // preparar
            executer.onExecuter(); // executar

            inExecution = true;

        }else{
            Toast mensagem = Toast.makeText(this, "Metronomo em execução.", Toast.LENGTH_SHORT);
            mensagem.show();
        }
    }

    /**
     * Para a execussão do metronomo
     * @param view
     */
    public void parar(View view){
        if(inExecution){
            executer.stopExecuter();
            inExecution = false;

        }else {
            Toast mensagem = Toast.makeText(this, "Todos os sons foram encerrados.", Toast.LENGTH_SHORT);
            mensagem.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(inExecution){
            executer.stopExecuter();
            inExecution = false;

        }

        super.onDestroy();
    }
}
