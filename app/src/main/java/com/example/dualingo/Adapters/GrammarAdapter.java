package com.example.dualingo.Adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dualingo.Models.Grammar;
import com.example.dualingo.R;
import java.util.List;
import java.util.stream.Collectors;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> {
    private List<Grammar> grammarList;

    public GrammarAdapter(List<Grammar> grammarList) {
        this.grammarList = grammarList;
    }

    @NonNull
    @Override
    public GrammarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grammar, parent, false);
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarViewHolder holder, int position) {
        Grammar grammar = grammarList.get(position);

        holder.tvTitle.setText(grammar.getTitle());
        holder.tvDescription.setText(grammar.getDescription());

        // Set công thức
        if (grammar.getFormula() != null) {
            String formulaText = "<b>Công thức:</b><br>" +
                    "Khẳng định: " + grammar.getFormula().getAffirmative() + "<br>" +
                    "Phủ định: " + grammar.getFormula().getNegative() + "<br>" +
                    "Nghi vấn: " + grammar.getFormula().getQuestion();
            holder.tvFormula.setText(Html.fromHtml(formulaText));
        }

        // Set các ví dụ và điểm lưu ý
        holder.tvExample.setText(Html.fromHtml("<b>Ví dụ:</b> " + grammar.getExample()));
        holder.tvKeyPoints.setText(Html.fromHtml("<b>Các điểm lưu ý:</b><br>" + String.join("<br>", grammar.getKeyPoints())));
        holder.tvDetailedExamples.setText(Html.fromHtml("<b>Ví dụ chi tiết:</b><br>" + grammar.getDetailedExamples().stream()
                .collect(Collectors.joining("<br>"))));

        // Hiển thị hoặc ẩn các trường dựa trên trạng thái mở rộng
        boolean isExpanded = grammar.isExpanded();
        holder.tvExample.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.tvKeyPoints.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.tvDetailedExamples.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Sự kiện nhấn vào để mở rộng/thu gọn
        holder.itemView.setOnClickListener(v -> {
            grammar.setExpanded(!isExpanded);
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return grammarList.size();
    }

    public static class GrammarViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvFormula, tvExample, tvKeyPoints, tvDetailedExamples;

        public GrammarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvFormula = itemView.findViewById(R.id.tvFormula);
            tvExample = itemView.findViewById(R.id.tvExample);
            tvKeyPoints = itemView.findViewById(R.id.tvKeyPoints);
            tvDetailedExamples = itemView.findViewById(R.id.tvDetailedExamples);
        }
    }
}
