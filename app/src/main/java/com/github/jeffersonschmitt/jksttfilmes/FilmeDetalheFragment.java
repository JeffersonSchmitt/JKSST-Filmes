package com.github.jeffersonschmitt.jksttfilmes;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.github.jeffersonschmitt.jksttfilmes.data.FilmesContract;

public class FilmeDetalheFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

  private Uri filmeUri;
  private TextView tituloView;
  private TextView descricaoView;
  private TextView dataView;
  private RatingBar avaliacaoView;
  private ImageView capaView;
  private ImageView posterview;
  private static final int FILME_DETALHE_LOADER=0;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      filmeUri = (Uri) getArguments().getParcelable(MainActivity.FILME_DETALHE_URI);
    }
    getLoaderManager().initLoader(FILME_DETALHE_LOADER,null,this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_filme_detalhe, container, false);

    tituloView = (TextView) view.findViewById(R.id.item_titulo);

    descricaoView = (TextView) view.findViewById(R.id.item_desc);

    dataView = (TextView) view.findViewById(R.id.item_data);

    avaliacaoView = (RatingBar) view.findViewById(R.id.item_avaliacao);

    capaView = (ImageView) view.findViewById(R.id.item_capa);

    posterview = (ImageView) view.findViewById(R.id.item_poster);


    return view;
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    String[] projection = {
        FilmesContract.FilmeEntry._ID,
        FilmesContract.FilmeEntry.COLUMN_TITULO,
        FilmesContract.FilmeEntry.COLUNM_DESCRICAO,
        FilmesContract.FilmeEntry.COLUMN_POSTER_PATH,
        FilmesContract.FilmeEntry.COLUMN_CAPA_PATH,
        FilmesContract.FilmeEntry.COLUMN_AVALIACAO,
        FilmesContract.FilmeEntry.COLUMN_DATA_LANCAMENTO,
    };
    return new CursorLoader(getContext(), filmeUri, projection, null, null, null);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    if(data==null || data.getCount()<1){
    return;
    }

    if(data.moveToFirst()){
      int tituloIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUMN_TITULO);
      int descricaoIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUNM_DESCRICAO);
      int posterIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUMN_POSTER_PATH);
      int capaIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUMN_DATA_LANCAMENTO);
      int avaliacaoIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUMN_AVALIACAO);
      int dataLancamentIndex= data.getColumnIndex(FilmesContract.FilmeEntry.COLUMN_DATA_LANCAMENTO);

      String titulo = data.getString(tituloIndex);
      String descricao= data.getString(descricaoIndex);
      String poster=data.getString(posterIndex);
      String capa=data.getString(capaIndex);
      float avaliacao=data.getFloat(avaliacaoIndex);
      String dataLancamento= data.getString(dataLancamentIndex);

      tituloView.setText(titulo);
      descricaoView.setText(descricao);
      avaliacaoView.setRating(avaliacao);
      dataView.setText(dataLancamento);

      new DownloadImageTask(capaView).execute(capa);

      if(posterview != null){
        new DownloadImageTask(posterview).execute(poster);
      }


    }
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {

  }
}