package com.daniel.sigue_med;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistroMedicamentoActivity
        extends AppCompatActivity {


// =====================================================
// COMPONENTES
// =====================================================

    private Spinner spinnerUnidad;

    private Spinner spinnerFrecuencia;

    private EditText editNombre;

    private EditText editDosis;

    private EditText editHoraInicio;

    private EditText editFechaInicio;

    private EditText editDiasTratamiento;

    private Button botonGuardar;


// =====================================================
// ID DEL MEDICAMENTO QUE SE ESTÁ EDITANDO
// =====================================================

    /*
     * -1 significa que estamos registrando
     * un medicamento nuevo.
     *
     * Si contiene otro número significa que
     * estamos editando un medicamento existente.
     */

    private int idMedicamentoEditar = -1;


// =====================================================
// ON CREATE
// =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // =================================================
        // CARGAR DISEÑO XML
        // =================================================

        setContentView(
                R.layout.activity_registro_medicamento
        );


        // =================================================
        // CONECTAR COMPONENTES CON XML
        // =================================================

        editNombre =
                findViewById(
                        R.id.editNombre
                );

        editDosis =
                findViewById(
                        R.id.editDosis
                );

        spinnerUnidad =
                findViewById(
                        R.id.spinnerUnidad
                );

        spinnerFrecuencia =
                findViewById(
                        R.id.spinnerFrecuencia
                );

        editHoraInicio =
                findViewById(
                        R.id.editHoraInicio
                );

        editFechaInicio =
                findViewById(
                        R.id.editFechaInicio
                );

        editDiasTratamiento =
                findViewById(
                        R.id.editDiasTratamiento
                );

        botonGuardar =
                findViewById(
                        R.id.botonGuardar
                );


        // =================================================
        // COMPROBAR SI ESTAMOS EDITANDO
        // =================================================

        /*
         * ListadoMedicamentosActivity envía mediante
         * Intent el ID del medicamento cuando pulsamos
         * el botón "Editar".
         *
         * Si no recibimos ningún ID, utilizamos -1.
         */

        idMedicamentoEditar =
                getIntent().getIntExtra(
                        "idMedicamento",
                        -1
                );


        // =================================================
        // CONFIGURAR SPINNER DE UNIDADES
        // =================================================

        String[] unidades = {

                "mg",
                "ml",
                "gotas",
                "comprimidos",
                "cápsulas"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(

                        this,

                        android.R.layout.simple_spinner_item,

                        unidades
                );


        /*
         * Define cómo se muestran las opciones
         * cuando abrimos el Spinner.
         */

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerUnidad.setAdapter(
                adapter
        );


        // =================================================
        // CONFIGURAR SPINNER DE FRECUENCIAS
        // =================================================

        String[] frecuencias = {

                "Cada 4 horas",
                "Cada 6 horas",
                "Cada 8 horas",
                "Cada 12 horas",
                "Cada 24 horas"
        };


        ArrayAdapter<String> adapterFrecuencia =
                new ArrayAdapter<>(

                        this,

                        android.R.layout.simple_spinner_item,

                        frecuencias
                );


        adapterFrecuencia.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerFrecuencia.setAdapter(
                adapterFrecuencia
        );


        // =================================================
        // CARGAR DATOS SI ESTAMOS EDITANDO
        // =================================================

        /*
         * Es importante hacer esto DESPUÉS de configurar
         * los Spinners.
         *
         * De esta manera cargarMedicamentoParaEditar()
         * puede seleccionar correctamente la unidad
         * y la frecuencia existentes.
         */

        if (idMedicamentoEditar != -1) {

            cargarMedicamentoParaEditar();
        }


        // =================================================
        // SELECTOR DE HORA
        // =================================================

        editHoraInicio.setOnClickListener(view -> {

            Calendar calendario =
                    Calendar.getInstance();


            int horaActual =
                    calendario.get(
                            Calendar.HOUR_OF_DAY
                    );


            int minutoActual =
                    calendario.get(
                            Calendar.MINUTE
                    );


            TimePickerDialog dialogoHora =
                    new TimePickerDialog(

                            RegistroMedicamentoActivity.this,

                            (view1, hourOfDay, minute) -> {

                                String hora =
                                        String.format(
                                                "%02d:%02d",
                                                hourOfDay,
                                                minute
                                        );


                                editHoraInicio.setText(
                                        hora
                                );
                            },

                            horaActual,

                            minutoActual,

                            true
                    );


            dialogoHora.show();
        });


        // =================================================
        // SELECTOR DE FECHA
        // =================================================

        editFechaInicio.setOnClickListener(view -> {

            Calendar calendario =
                    Calendar.getInstance();


            int año =
                    calendario.get(
                            Calendar.YEAR
                    );


            int mes =
                    calendario.get(
                            Calendar.MONTH
                    );


            int dia =
                    calendario.get(
                            Calendar.DAY_OF_MONTH
                    );


            DatePickerDialog dialogoFecha =
                    new DatePickerDialog(

                            RegistroMedicamentoActivity.this,

                            (view1, year, month, dayOfMonth) -> {

                                /*
                                 * Calendar utiliza meses desde 0.
                                 *
                                 * Por eso añadimos 1 al mes.
                                 */

                                String fecha =
                                        String.format(

                                                "%04d-%02d-%02d",

                                                year,

                                                month + 1,

                                                dayOfMonth
                                        );


                                editFechaInicio.setText(
                                        fecha
                                );
                            },

                            año,

                            mes,

                            dia
                    );


            dialogoFecha.show();
        });


        // =================================================
        // BOTÓN GUARDAR
        // =================================================

        botonGuardar.setOnClickListener(view -> {

            guardarOActualizarMedicamento();

        });
    }


// =====================================================
// GUARDAR O ACTUALIZAR MEDICAMENTO
// =====================================================

    private void guardarOActualizarMedicamento() {


        // =================================================
        // OBTENER NOMBRE
        // =================================================

        String nombre =
                editNombre.getText()
                        .toString()
                        .trim();


        if (nombre.isEmpty()) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce el nombre del medicamento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // OBTENER DOSIS
        // =================================================

        String textoDosis =
                editDosis.getText()
                        .toString()
                        .trim();


        if (textoDosis.isEmpty()) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce la dosis",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        double dosis;


        try {

            dosis =
                    Double.parseDouble(
                            textoDosis
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce una dosis numérica válida",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        if (dosis <= 0) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "La dosis debe ser mayor que 0",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // OBTENER UNIDAD
        // =================================================

        String unidad =
                spinnerUnidad
                        .getSelectedItem()
                        .toString();


        // =================================================
        // OBTENER FRECUENCIA
        // =================================================

        String frecuencia =
                spinnerFrecuencia
                        .getSelectedItem()
                        .toString();


        // =================================================
        // OBTENER HORA
        // =================================================

        String horaInicio =
                editHoraInicio.getText()
                        .toString()
                        .trim();


        if (horaInicio.isEmpty()) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce la hora de inicio del tratamiento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // OBTENER FECHA
        // =================================================

        String fechaInicio =
                editFechaInicio.getText()
                        .toString()
                        .trim();


        if (fechaInicio.isEmpty()) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce la fecha de inicio del tratamiento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // OBTENER DÍAS DE TRATAMIENTO
        // =================================================

        String textoDias =
                editDiasTratamiento.getText()
                        .toString()
                        .trim();


        if (textoDias.isEmpty()) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce los días de tratamiento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        int diasTratamiento;


        try {

            diasTratamiento =
                    Integer.parseInt(
                            textoDias
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Introduce un número de días válido",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        if (diasTratamiento <= 0) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "El número de días debe ser mayor que 0",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // CREAR OBJETO MEDICAMENTO
        // =================================================

        Medicamento medicamento =
                new Medicamento(

                        nombre,

                        dosis,

                        unidad,

                        frecuencia,

                        horaInicio,

                        fechaInicio,

                        diasTratamiento
                );


        // =================================================
        // MEDICAMENTO NUEVO
        // =================================================

        if (idMedicamentoEditar == -1) {

            guardarMedicamentoNuevo(
                    medicamento
            );

            return;
        }


        // =================================================
        // MEDICAMENTO EXISTENTE
        // =================================================

        editarMedicamento(
                medicamento
        );
    }


// =====================================================
// GUARDAR MEDICAMENTO NUEVO
// =====================================================

    private void guardarMedicamentoNuevo(
            Medicamento medicamento) {


        // =================================================
        // INSERTAR EN SQLITE
        // =================================================

        long id =
                GestorMedicamentos.guardarMedicamento(

                        RegistroMedicamentoActivity.this,

                        medicamento
                );


        // =================================================
        // COMPROBAR RESULTADO
        // =================================================

        /*
         * SQLite devuelve -1 si el INSERT ha fallado.
         */

        if (id == -1) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "Error al guardar el medicamento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // ASIGNAR ID GENERADO
        // =================================================

        medicamento.setId(
                (int) id
        );


        // =================================================
        // CREAR TOMAS
        // =================================================

        crearTomas(
                medicamento
        );


        // =================================================
        // PROGRAMAR ALARMAS
        // =================================================

        GestorAlarmas.programarAlarmas(

                RegistroMedicamentoActivity.this,

                medicamento
        );


        // =================================================
        // MENSAJE
        // =================================================

        Toast.makeText(

                RegistroMedicamentoActivity.this,

                "Medicamento guardado correctamente",

                Toast.LENGTH_SHORT

        ).show();


        // =================================================
        // VOLVER AL LISTADO
        // =================================================

        finish();
    }


// =====================================================
// EDITAR MEDICAMENTO
// =====================================================

    private void editarMedicamento(
            Medicamento medicamento) {


        // =================================================
        // ASIGNAR ID EXISTENTE
        // =================================================

        medicamento.setId(
                idMedicamentoEditar
        );


        // =================================================
        // OBTENER MEDICAMENTO ANTERIOR
        // =================================================

        /*
         * Necesitamos conservar algunos datos del medicamento
         * anterior antes de actualizarlo.
         *
         * Especialmente alarmaActiva.
         */

        Medicamento medicamentoAnterior =
                GestorMedicamentos.obtenerMedicamentoPorId(

                        RegistroMedicamentoActivity.this,

                        idMedicamentoEditar
                );


        // =================================================
        // COMPROBAR QUE EXISTE
        // =================================================

        if (medicamentoAnterior == null) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "No se ha encontrado el medicamento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // CONSERVAR ESTADO DE LA ALARMA
        // =================================================

        /*
         * El objeto Medicamento que acabamos de crear
         * contiene los datos escritos en el formulario,
         * pero no necesariamente conoce el estado anterior
         * de la alarma.
         *
         * Por eso copiamos el valor del medicamento anterior.
         */

        medicamento.setAlarmaActiva(
                medicamentoAnterior.isAlarmaActiva()
        );


        // =================================================
        // CANCELAR ALARMAS ANTIGUAS
        // =================================================

        /*
         * Antes de modificar los datos cancelamos las alarmas
         * correspondientes al medicamento anterior.
         *
         * Así evitamos que las alarmas antiguas continúen
         * funcionando después de cambiar la frecuencia,
         * fecha u hora.
         */

        GestorAlarmas.cancelarAlarmas(

                RegistroMedicamentoActivity.this,

                medicamentoAnterior
        );


        // =================================================
        // ELIMINAR TOMAS ANTIGUAS
        // =================================================

        /*
         * Las tomas antiguas ya no son necesariamente válidas
         * porque el usuario puede haber cambiado:
         *
         * - fecha
         * - hora
         * - frecuencia
         * - duración
         *
         * Por eso las eliminamos antes de crear las nuevas.
         */

        GestorTomas.eliminarTomasPorMedicamento(

                RegistroMedicamentoActivity.this,

                idMedicamentoEditar
        );


        // =================================================
        // ACTUALIZAR MEDICAMENTO
        // =================================================

        /*
         * GestorMedicamentos.actualizarMedicamento()
         * devuelve ahora el número de registros afectados.
         *
         * Lo comprobamos para saber si SQLite realmente
         * ha actualizado el medicamento.
         */

        int filasActualizadas =
                GestorMedicamentos.actualizarMedicamento(

                        RegistroMedicamentoActivity.this,

                        medicamento
                );


        // =================================================
        // COMPROBAR ACTUALIZACIÓN
        // =================================================

        if (filasActualizadas <= 0) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "No se ha podido actualizar el medicamento",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }


        // =================================================
        // CREAR NUEVAS TOMAS
        // =================================================

        crearTomas(
                medicamento
        );


        // =================================================
        // PROGRAMAR NUEVAS ALARMAS
        // =================================================

        /*
         * GestorAlarmas comprobará el estado de alarmaActiva.
         *
         * Como hemos conservado el valor anterior:
         *
         * true  -> se vuelven a programar.
         * false -> permanecen desactivadas.
         */

        GestorAlarmas.programarAlarmas(

                RegistroMedicamentoActivity.this,

                medicamento
        );


        // =================================================
        // MENSAJE
        // =================================================

        Toast.makeText(

                RegistroMedicamentoActivity.this,

                "Medicamento actualizado correctamente",

                Toast.LENGTH_SHORT

        ).show();


        // =================================================
        // VOLVER AL LISTADO
        // =================================================

        finish();
    }


// =====================================================
// CREAR TOMAS
// =====================================================

    private void crearTomas(
            Medicamento medicamento) {


        // =================================================
        // CALCULAR TOMAS
        // =================================================

        ArrayList<LocalDateTime> tomasCalculadas =
                CalculadorTomas.calcularTomas(

                        medicamento.getFechaInicio(),

                        medicamento.getHoraInicio(),

                        medicamento.getFrecuencia(),

                        medicamento.getDiasTratamiento()
                );


        // =================================================
        // FORMATO DE FECHA Y HORA
        // =================================================

        /*
         * Utilizamos SIEMPRE el mismo formato:
         *
         * dd/MM/yyyy HH:mm
         *
         * Esto es importante porque GestorTomas utiliza
         * fechaHora para comprobar si una toma ya existe.
         */

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );


        // =================================================
        // RECORRER TOMAS
        // =================================================

        for (
                LocalDateTime tomaCalculada :
                tomasCalculadas
        ) {


            // =================================================
            // CONVERTIR FECHA/HORA A TEXTO
            // =================================================

            String fechaHora =
                    tomaCalculada.format(
                            formato
                    );


            // =================================================
            // COMPROBAR SI YA EXISTE
            // =================================================

            boolean existe =
                    GestorTomas.existeToma(

                            RegistroMedicamentoActivity.this,

                            medicamento.getId(),

                            fechaHora
                    );


            // =================================================
            // GUARDAR TOMA
            // =================================================

            if (!existe) {

                Toma toma =
                        new Toma(

                                medicamento.getId(),

                                fechaHora,

                                EstadoToma.PENDIENTE
                        );


                GestorTomas.guardarToma(

                        RegistroMedicamentoActivity.this,

                        toma
                );
            }
        }
    }


// =====================================================
// CARGAR MEDICAMENTO PARA EDITAR
// =====================================================

    private void cargarMedicamentoParaEditar() {


        // =================================================
        // OBTENER MEDICAMENTO
        // =================================================

        Medicamento medicamento =
                GestorMedicamentos.obtenerMedicamentoPorId(

                        RegistroMedicamentoActivity.this,

                        idMedicamentoEditar
                );


        // =================================================
        // COMPROBAR EXISTENCIA
        // =================================================

        if (medicamento == null) {

            Toast.makeText(

                    RegistroMedicamentoActivity.this,

                    "No se ha encontrado el medicamento",

                    Toast.LENGTH_SHORT

            ).show();


            finish();

            return;
        }


        // =================================================
        // CARGAR NOMBRE
        // =================================================

        editNombre.setText(
                medicamento.getNombre()
        );


        // =================================================
        // CARGAR DOSIS
        // =================================================

        editDosis.setText(
                String.valueOf(
                        medicamento.getDosis()
                )
        );


        // =================================================
        // CARGAR HORA
        // =================================================

        editHoraInicio.setText(
                medicamento.getHoraInicio()
        );


        // =================================================
        // CARGAR FECHA
        // =================================================

        editFechaInicio.setText(
                medicamento.getFechaInicio()
        );


        // =================================================
        // CARGAR DÍAS DE TRATAMIENTO
        // =================================================

        editDiasTratamiento.setText(
                String.valueOf(
                        medicamento.getDiasTratamiento()
                )
        );


        // =================================================
        // SELECCIONAR UNIDAD
        // =================================================

        /*
         * Los Spinners empiezan en la posición 0.
         *
         * Recorremos todas sus opciones buscando la que
         * coincide con la almacenada en SQLite.
         */

        for (
                int i = 0;
                i < spinnerUnidad.getCount();
                i++
        ) {

            if (

                    spinnerUnidad
                            .getItemAtPosition(i)
                            .toString()
                            .equals(
                                    medicamento.getUnidad()
                            )

            ) {

                spinnerUnidad.setSelection(i);

                break;
            }
        }


        // =================================================
        // SELECCIONAR FRECUENCIA
        // =================================================

        for (
                int i = 0;
                i < spinnerFrecuencia.getCount();
                i++
        ) {

            if (

                    spinnerFrecuencia
                            .getItemAtPosition(i)
                            .toString()
                            .equals(
                                    medicamento.getFrecuencia()
                            )

            ) {

                spinnerFrecuencia.setSelection(i);

                break;
            }
        }


        // =================================================
        // NOTA SOBRE alarmaActiva
        // =================================================

        /*
         * No necesitamos mostrar alarmaActiva en este
         * formulario.
         *
         * El estado se conserva internamente y se recupera
         * desde SQLite mediante:
         *
         * medicamento.isAlarmaActiva()
         *
         * cuando guardamos los cambios.
         */
    }
}