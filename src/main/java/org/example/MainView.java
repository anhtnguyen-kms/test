package org.example;

import com.sun.jna.platform.win32.GL;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.editor.DataGridEditor;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.data.grid.DataGridItems;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
//import io.jmix.flowui.view.DefaultMainViewParent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;


@Route(value = "persons")
@ViewDescriptor("main-view.xml")
@ViewController("MainView.detail")
public class MainView extends StandardView {

    @Autowired
    private UiComponents uiComponents;


//    private final List<Item> items = new ArrayList<>();

    private List<String> comboBoxItems = new ArrayList<>(); // This is safe and per-instance


    @ViewComponent
    private CollectionContainer<Item> glCashAccountingMapDc;
    @ViewComponent
    private DataGrid<Item> glCashAccountingMapDataGrid;

    private final Map<String, Component> editorComponents = new HashMap<>();

    Item item1 = new Item("Supplier 1", "Item 1", "true");
    Item item2 = new Item("Supplier 2", "Item 2", "false");
    Item item3 = new Item("Supplier 1", "Item 3", "false");

    public MainView() {
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        System.out.println("onBeforeShow");
        loadItemsWithCompletableFuture(null);
        glCashAccountingMapDataGrid.getEditor().editItem(item1); // Open editor for the first item
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        System.out.println("onInit");
        // Configure the ComboBox for supplierId
        // Configure the TextField for supplierName
        glCashAccountingMapDataGrid.setEnabled(true);
        glCashAccountingMapDataGrid.getEditor().setBuffered(true);

        glCashAccountingMapDataGrid.getEditor().setColumnEditorComponent("supplierId", context -> {
            JmixComboBox<String> supplierIdComboBox = uiComponents.create(JmixComboBox.class);
            supplierIdComboBox.setPlaceholder("Select supplier");
            supplierIdComboBox.setAllowCustomValue(true);
            supplierIdComboBox.setItems(comboBoxItems); // Use the instance variable
            supplierIdComboBox.setValue(context.getItem().getSupplierId());
            editorComponents.put("supplierId", supplierIdComboBox); // Store reference
            return supplierIdComboBox;
        });

        glCashAccountingMapDataGrid.getEditor().setColumnEditorComponent("supplierName", context -> {
            TextField textField = new TextField();
            textField.setPlaceholder("Enter supplier name");
            textField.setValue(context.getItem().getSupplierName());
            editorComponents.put("supplierName", textField); // Store reference
            return textField;
        });

        glCashAccountingMapDataGrid.getEditor().setColumnEditorComponent("branchCode", context -> {
            EntityComboBox<String> branchSearchComboBox = uiComponents.create(EntityComboBox.class);
            branchSearchComboBox.setPlaceholder("Enter branch code");
            branchSearchComboBox.setAllowCustomValue(true);
//            branchSearchComboBox.setValue(context.getItem().getBranchCode());
            branchSearchComboBox.setPageSize(10);
            // Set page size for the dropdown
            branchSearchComboBox.setItemsFetchCallback(query -> {

                String filter = query.getFilter().orElse("");
                int offset = query.getOffset();
                int limit = query.getLimit();
                return searchCustomers(filter).stream();
            });
            editorComponents.put("branchCode", branchSearchComboBox); // Store reference

            return branchSearchComboBox;
        });

        glCashAccountingMapDc.getMutableItems().add(item1);
        glCashAccountingMapDc.getMutableItems().add(item2);
        glCashAccountingMapDc.getMutableItems().add(item3);

        glCashAccountingMapDataGrid.getEditor().addOpenListener(event2 -> {
            System.out.println("Editor opened 123");
            Focusable supplierIdComponent = (Focusable) editorComponents.get("supplierId");
            supplierIdComponent.focus(); // Focus on the supplierId ComboBox
        });
    }

    @Subscribe("createBtn")
    public void onCreateBtnClick(ClickEvent event) {
        System.out.println("Create button clicked 123");
        Item newItem = new Item("Supplier 1", "Item 5", "false");
        newItem.setBranchCode("Data12"); // Set default branch code

        // Add the new item to the data container
        glCashAccountingMapDataGrid.getEditor().cancel();
        glCashAccountingMapDc.getMutableItems().add(newItem);
        glCashAccountingMapDataGrid.getEditor().editItem(newItem);
    }

    private List<String> searchCustomers(String filter) {
        System.out.println("Searching for: " + filter); // should trigger every time user types
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException ie) {
            return new ArrayList<>();
        }
        if (filter == null || filter.isEmpty()) {
            return new ArrayList<>();
        }
        if (filter.startsWith("T")) {
            return List.of("Test11", "Test12", "Test13");
        }
        if (filter.startsWith("D")) {
            return List.of("Data11", "Data12", "Data13");

        }
        return new ArrayList<>();
    }

    private void loadItemsWithCompletableFuture(ComboBox<String> comboBox) {
//        comboBox.setPlaceholder("Loading...");
        CompletableFuture.supplyAsync(() -> {
            // Call API or long-running task
            try {
                Thread.sleep(3000); // Simulate delay
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return List.of("Supplier 1", "Supplier 2", "Supplier 3");
        }).thenAccept(items -> {
            comboBoxItems.addAll(items);
            glCashAccountingMapDataGrid.getEditor().editItem(item1); // Open editor for the first item
        });
    }

}