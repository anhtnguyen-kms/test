package org.example;



import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
//import com.vaadin.flow.data.provider.CallbackDataProvider;
//import com.vaadin.flow.data.provider.Query;

import java.util.List;
import java.util.function.Function;

public class SearchableComboBox<T> extends ComboBox<T> {

    public SearchableComboBox(String label,
                              Function<String, List<T>> searchFunction,
                              Function<T, String> itemLabelGenerator) {

        setLabel(label);
        setClearButtonVisible(true);
        setItemLabelGenerator(test -> test.toString());

        // Use fromFilteringCallbacks, cast to DataProvider<T, ?>
//        DataProvider<T, String> filteringDataProvider = DataProvider.fromFilteringCallbacks(
//                query -> {
//                    String filter = query.getFilter().orElse("");
//                    return searchFunction.apply(filter).stream();
//                },
//                filter -> 0 // unused, but required by API
//        );

        // Safe cast because ComboBox doesn't use the filter type directly
        // Provide fetch and count callbacks
        DataProvider<T, String> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    String filter = (String)query.getFilter().orElse("");
                    List<T> results = searchFunction.apply(filter);
                    return results.stream();
                },
                filter -> 5
        );

        // This is REQUIRED — must convert typed string into the filter type (String → String here)
        setDataProvider(dataProvider, filterText -> filterText);

    }
}

