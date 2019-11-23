package sample;

import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Controller {

    ArrayList<Vertex> listv = new ArrayList<Vertex>();
    ArrayList<Edge> liste = new ArrayList<>();

    //Add
    @FXML
    Button add_edge_button;
    @FXML
    Button add_vertex_button;
    @FXML
    TextField add_x;
    @FXML
    TextField add_y;
    @FXML
    TextField add_name;
    @FXML
    TextField add_from;
    @FXML
    TextField add_to;
    @FXML
    TextField add_wt;

    //Search
    @FXML
    Button search_edge_button;
    @FXML
    Button search_vertex_button;
    @FXML
    TextField search_name;
    @FXML
    TextField search_from;
    @FXML
    TextField search_to;
    @FXML
    Text Search_vertex_details;
    @FXML
    Text Search_edge_details;

    //
    @FXML
    Button modify_edge_button;
    @FXML
    Button modify_vertex_button;
    @FXML
    TextField modify_x;
    @FXML
    TextField modify_y;
    @FXML
    TextField modify_name;
    @FXML
    TextField modify_from;
    @FXML
    TextField modify_to;
    @FXML
    TextField modify_wt;
    ;

    // Delete

    //
    @FXML
    Button delete_edge_button;
    @FXML
    Button delete_vertex_button;
    @FXML
    TextField delete_name;
    @FXML
    TextField delete_from;
    @FXML
    TextField delete_to;

    // Print
    @FXML
    Button print_path_button;
    @FXML
    TextField print_from;
    @FXML
    Text print_path;
    @FXML
    TextField print_to;

    // File

    @FXML
    Button imports;
    @FXML
    Button export;

    @FXML
    AnchorPane surface;
    @FXML
    TextField vertex_name;
    @FXML
    Button reset;
    @FXML
    Button Start;
    @FXML
    TextField edge_wt;
    @FXML
    TextField nFrom;
    @FXML
    TextField nTo;


    MyEdge l;
    ArrayList<MyCircle> listc = new ArrayList<>();
    ArrayList<MyEdge> listl = new ArrayList<>();
    ArrayList<Vertex> listv_new = new ArrayList<>();
    ArrayList<Edge> liste_new = new ArrayList<>();
    Map<MyCircle,Text> vmap = new HashMap<>();
    Map<MyEdge,Text> emap = new HashMap<>();
    ArrayList<Integer> mpath;
    ArrayList<MyEdge> pathEdges = new ArrayList<>();
    ArrayList<MyCircle> pathvertex = new ArrayList<>();
    EventHandler<MouseEvent> mouseclick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            Rectangle r = new Rectangle();
            r.setX(600);
            r.setY(0);
            r.setWidth(200);
            r.setHeight(600);
            MyCircle c = new MyCircle("asd");
            c.setCenterX(e.getX());
            c.setCenterY(e.getY());
            c.setRadius(15);

            if(c.intersects(r.getLayoutBounds())) {
                System.out.println("intersection");
                return;
            }


            for(MyEdge l : listl)
            {
                if(l.intersects(c.getLayoutBounds())) {
                    System.out.println("Edge present");
                    return;
                }
            }
            for(MyCircle c1 : listc)
            {
                if(c1.intersects(c.getLayoutBounds()))
                {
                    System.out.println(e.getSceneX()+" "+ e.getSceneY());
                    return;
                }

            }
            String name = vertex_name.getText();
            if(name.isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("EMPTY NAME!!");
                alert.setContentText("Please Enter Vertex Name");
                alert.showAndWait();
                return ;
            }
            vertex_name.clear();
            c.setName(name.toUpperCase());
            c.addEventFilter(MouseEvent.MOUSE_PRESSED,mousepress);
            c.addEventFilter(MouseEvent.MOUSE_RELEASED,mouserelease);
            listc.add(c);
            Text t = new Text(name.toUpperCase());
            t.setX(c.getCenterX()-5);
            t.setY(c.getCenterY()-25);
            surface.getChildren().addAll(c,t);
            vmap.put(c,t);
        }
    };
    EventHandler<MouseEvent> mousepress = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            try {
                int wt = Integer.parseInt(edge_wt.getText());
                l = new MyEdge(wt);
            }catch (NumberFormatException e1)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("INVALID OR EMPTY WEIGHT");
                alert.setContentText("Please Enter Valid Weight");
                alert.showAndWait();
                edge_wt.clear();
                return ;
            }
            for(MyCircle c: listc)
            {
                if(c.contains(e.getX(),e.getY()))
                {
                    l.setStartX(c.getCenterX());
                    l.setStartY(c.getCenterY());
                    l.setFrom(c.getName());
                    return;
                }
            }

        }
    };
    EventHandler<MouseEvent> mousedelete = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            for( MyCircle c: listc)
            {
                if(c.contains(e.getX(),e.getY()))
                {
                    ArrayList<MyEdge> temp = new ArrayList<>();
                    for(MyEdge l : listl)
                    {
                        if((l.getEndX()==c.getCenterX()&&l.getEndY()==c.getCenterY())||(l.getStartX()==c.getCenterX()&&l.getStartY()==c.getCenterY())){
                            surface.getChildren().remove(l);
                            surface.getChildren().remove(emap.get(l));
                            emap.remove(l);
                            temp.add(l);
                        }
                    }
                    listl.removeAll(temp);
                    surface.getChildren().remove(vmap.get(c));
                    surface.getChildren().remove(c);
                    vmap.remove(c);
                    listc.remove(c);
                    break;
                }
            }
        }
    };
    EventHandler<KeyEvent> keyinput = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent k) {
            if(k.getCode() == KeyCode.A)
            {
                System.out.println("A pressed");
                addEvents();
            }

            if(k.getCode()==KeyCode.D) {
                System.out.println("D pressed");
                deleteEvents();
            }
            /*if(k.getCode()==KeyCode.M)
                modifyEvents();*/
        }
    };
    EventHandler<MouseEvent> mouserelease = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            int f = 0;
            Text t=null;
            for(MyCircle c: listc)
            {
                if(c.contains(e.getX(),e.getY()))
                {
                    l.setEndX(c.getCenterX());
                    l.setEndY(c.getCenterY());
                    l.endXProperty().bind(c.centerXProperty());
                    l.endYProperty().bind(c.centerYProperty());
                    l.setTo(c.getName());
                    t = new Text();
                    t.setText(edge_wt.getText());
                    t.setX((l.getStartX()+l.getEndX())/2+10);
                    t.setY((l.getStartY()+l.getEndY())/2+10);
                    edge_wt.clear();
                    listl.add(l);
                    emap.put(l,t);
                    f = 1;
                    break;
                }
            }
            if(f==1)
                surface.getChildren().addAll(l,t);
        }
    };
    EventHandler<MouseEvent> edgedelete  = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            for(MyEdge l : listl)
            {
                if(l.contains(e.getX(),e.getY())) {
                    surface.getChildren().remove(l);
                    surface.getChildren().remove(emap.get(l));
                    emap.remove(l);
                    listl.remove(l);
                    break;
                }
            }
        }
    };
    public void Animate()
    {
        dijkstra2();
        Double[] arr = new Double[2*(pathEdges.size()+1)];

        Double startx = pathvertex.get(0).getCenterX();
        Double starty = pathvertex.get(0).getCenterY();
        System.out.println(startx + " "+starty);

        int j = 0;
        for(MyCircle c : pathvertex)
        {
            arr[j++]=c.getCenterX();
            arr[j++]=c.getCenterY();
        }

        Polyline polyline = new Polyline();
        polyline.getPoints().addAll(arr);

        Circle c = new Circle();
        c.setRadius(10);
        c.setStroke(Color.RED);
        c.setCenterX(startx);
        c.setCenterY(starty);
        surface.getChildren().addAll(c);
        PathTransition t = new PathTransition();
        t.setNode(c);
        t.setDuration(Duration.seconds(4));
        t.setPath(polyline);
        t.setCycleCount(PathTransition.INDEFINITE);
        t.play();

    }
    public void addEvents()
    {
        surface.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseclick);
        //surface.addEventHandler(KeyEvent.KEY_PRESSED,keyinput);
        for(MyCircle c : listc)
        {
            c.addEventFilter(MouseEvent.MOUSE_PRESSED,mousepress);
            c.addEventFilter(MouseEvent.MOUSE_RELEASED,mouserelease);
        }
    }
    public void deleteEvents()
    {
        surface.removeEventFilter(MouseEvent.MOUSE_CLICKED,mouseclick);
        for(MyCircle c : listc)
        {
            //c.removeEventFilter(MouseEvent.MOUSE_CLICKED,mouseclick);
            c.removeEventFilter(MouseEvent.MOUSE_PRESSED,mousepress);
            c.removeEventFilter(MouseEvent.MOUSE_RELEASED,mouserelease);
            c.addEventFilter(MouseEvent.MOUSE_CLICKED,mousedelete);
        }
        for(MyEdge l:listl)
        {
            l.addEventFilter(MouseEvent.MOUSE_CLICKED,edgedelete);
        }
    }


    public void trying()
    {
        //surface.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseclick);
        surface.getScene().addEventHandler(KeyEvent.KEY_PRESSED,keyinput);
       // surface.addEventFilter(MouseDragEvent.,mousedrag);
    }

    public void ResetGraph()
    {
        surface.getChildren().removeAll(listc);
        surface.getChildren().removeAll(listl);
        surface.getChildren().removeAll(emap.values());
        surface.getChildren().removeAll(vmap.values());
        listc.clear();
        listl.clear();
        emap.clear();
        vmap.clear();
        surface.removeEventFilter(MouseEvent.MOUSE_CLICKED,mouseclick);
        trying();

    }

    public void AddVertex() {
        try {
            int x = Integer.parseInt(add_x.getText());
            int y = Integer.parseInt(add_y.getText());
            String name = add_name.getText();
            if(name.isEmpty())
                throw new NullPointerException();
            add_x.clear();
            add_y.clear();
            add_name.clear();
            Vertex v = new Vertex(x, y, name);
            for(Vertex v1: listv)
            {
                if(v1.getName().equals(name))
                    throw new Exception();
            }
            listv.add(v);
            System.out.println("added");
        } catch (NumberFormatException e) {
            add_x.clear();
            add_y.clear();
            add_name.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("ILLEGAL INPUT!!");
            alert.setContentText("Please enter integer values in coordinates field");
            alert.showAndWait();

        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EMPTY INPUT!!");
            alert.setContentText("Please Enter Valid Input");
            alert.showAndWait();
        } catch (Exception e) {

            add_name.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Vertex Name Already Present!!");
            alert.setContentText("Please Enter Different Vertex Name");
            alert.showAndWait();
        }

    }

    public void AddEdge() {
        try {
            String from = add_from.getText();
            String to = add_to.getText();
            int wt = Integer.parseInt(add_wt.getText());
            int f = 0;
            for(Vertex v : listv)
            {
                 if(v.getName().equals(from) || v.getName().equals(to))
                 {
                     f++;
                 }
            }
            if(f<2)
            {
                add_from.clear();
                add_to.clear();
                add_wt.clear();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("VERTEX ENTERED DOES NOT EXIST");
                alert.setContentText("Please Enter Correct Vertices");
                alert.showAndWait();
                return;
            }
            add_from.clear();
            add_to.clear();
            add_wt.clear();
            for(Edge e11: liste)
            {
                if(e11.getFrom().equals(from) &&  e11.getTo().equals(to))
                {
                    throw new Exception();
                }
            }
            Edge e = new Edge(from, to, wt);
            Edge e1 = new Edge(to, from, wt);

            liste.add(e);
            liste.add(e1);
            System.out.println("added");
        } catch (NumberFormatException e) {
            add_from.clear();
            add_to.clear();
            add_wt.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("ILLEGAL INPUT!!");
            alert.setContentText("Please enter integer value in weight field");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EMPTY INPUT!!");
            alert.setContentText("Please Enter Valid Input");
            alert.showAndWait();
        }catch (Exception e) {
            add_from.clear();
            add_to.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EDGE ALREADY PRESENT!!");
            alert.setContentText("Please Enter Different Vertices");
            alert.showAndWait();
        }
    }

    public void SearchVertex() {
        try {
            String name = search_name.getText();
            if(name.isEmpty())
                throw new NullPointerException();
            int f = 0;
            for (Vertex vy : listv) {
                if (name.equals(vy.getName())) {
                    Search_vertex_details.setText("Vertex Found With Following Details:\n" + "Name: " + name + "\nX-Co-ordinate : " + vy.getX_coordinate() + "\nY-Coordinate : " + vy.getY_coordinate());
                    f = 1;
                    break;
                }
            }
            if (f == 0) {
                Search_vertex_details.setText("Vertex Not Found");
            }
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EMPTY INPUT!!");
            alert.setContentText("Please Enter Valid Input");
            alert.showAndWait();
        }
    }

    public void SearchEdge() {
       try {
           String from = search_from.getText();
           String to = search_to.getText();
           if(from.isEmpty() || to.isEmpty())
               throw new NullPointerException();
           System.out.println("Searching");
           int f = 0;
           for (Edge x : liste) {
               System.out.println(x.getFrom() + " " + x.getTo() + "\n" + from + " " + to);
               if (x.getFrom().equals(from) && x.getTo().equals(to)) {
                   Search_edge_details.setText("Edge Found With Following Details:\n" + "From : " + x.getFrom() + "\n" + "To : " + x.getTo() + "\n" + "Weight : " + x.getWt());
                   System.out.println("Searched");
                   f = 1;
                   break;
               }
           }
           if (f == 0) {
               Search_edge_details.setText("Edge not Found ");
           }
       }catch (NullPointerException e) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("EMPTY INPUT!!");
           alert.setContentText("Please Enter Valid Input");
           alert.showAndWait();
       }
    }

    public void ModifyEdge() {
       try {
           String from = modify_from.getText();
           String to = modify_to.getText();
           if(from.isEmpty() || to.isEmpty())
               throw new NullPointerException();
           int wt = Integer.parseInt(modify_wt.getText());
           modify_from.clear();
           modify_to.clear();
           modify_wt.clear();
           int flag = 0;
           for (Edge e : liste) {
               if (e.getFrom().equals(from) && e.getTo().equals(to)) {
                   e.setWt(wt);
                   flag = 1;
                   break;
               }
           }
           if(flag == 0 )
           {
               throw new Exception();
           }

       }catch (NumberFormatException e)
       {
           add_from.clear();
           add_to.clear();
           add_wt.clear();
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("ILLEGAL INPUT!!");
           alert.setContentText("Please enter integer value in weight field");
           alert.showAndWait();
       }catch (NullPointerException e) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("EMPTY INPUT!!");
           alert.setContentText("Please Enter Valid Input");
           alert.showAndWait();
       } catch (Exception e) {
           modify_from.clear();
           modify_to.clear();
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("EDGE NOT FOUND");
           alert.setContentText("Please Enter Correct Edge Details To Be Modified");
           alert.showAndWait();
       }


    }

    public void ModifyVertex() {
       try {
           String name = modify_name.getText();
           if(name.isEmpty())
               throw new NullPointerException();
           int x = Integer.parseInt(modify_x.getText());
           int y = Integer.parseInt(modify_y.getText());

           modify_x.clear();
           modify_name.clear();
           modify_y.clear();
           int flag = 0;

           for (Vertex v : listv) {
               if (v.getName().equals(name)) {
                   v.setX_coordinate(x);
                   v.setY_coordinate(y);
                   flag=1;
               }
           }
           if(flag == 0)
           {
               throw new Exception();
           }
       }catch (NumberFormatException e)
       {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("ILLEGAL INPUT!!");
           alert.setContentText("Please enter integer values in coordinates field");
           alert.showAndWait();
       } catch (NullPointerException e) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("EMPTY INPUT!!");
           alert.setContentText("Please Enter Valid Input");
           alert.showAndWait();
       }catch (Exception e) {
           modify_name.clear();
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText("VERTEX NOT FOUND");
           alert.setContentText("Please Enter Correct Vertex Name To Be Modified");
           alert.showAndWait();
       }
    }

    public void DeleteEdge() {
        try {
            String from = delete_from.getText();
            String to = delete_to.getText();
            if(from.isEmpty() || to.isEmpty())
                throw new NullPointerException();
            delete_from.clear();
            delete_to.clear();
            int flag = 0;
            for (Edge e : liste) {
                if (from.equals(e.getFrom()) && to.equals(e.getTo())) {

                    liste.remove(e);
                    flag=1;
                    System.out.println("deleted");
                    break;

                }
            }
            if(flag == 0)
            {
                throw new Exception();
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("ILLEGAL INPUT!!");
            alert.setContentText("Please Enter Valid Edge Details");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EDGE NOT FOUND");
            alert.setContentText("Please Enter Valid Edge Details");
            alert.showAndWait();
        }
    }

    public void DeleteVertex() {
        try {
            String name = delete_name.getText();
            if(name.isEmpty())
                throw new NullPointerException();
            delete_name.clear();
            int flag = 0;
            for (Vertex v : listv) {
                if (name.equals(v.getName())) {
                    listv.remove(v);
                    System.out.println("deleted");
                    flag = 1;
                    break;
                }
            }
            if(flag !=1)
            {
                System.out.println(flag);
                throw new Exception();
            }
            ArrayList<Edge> temp = new ArrayList<>();
            for(Edge e : liste)
            {
                if(name.equals(e.getFrom()) || name.equals(e.getTo()))
                {
                    temp.add(e);
                }
            }
            liste.removeAll(temp);

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("ILLEGAL INPUT!!");
            alert.setContentText("Please Enter Valid Vertex Name");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("VERTEX NOT FOUND");
            alert.setContentText("Please Enter Valid Vertex Name");
            alert.showAndWait();
           // e.printStackTrace();
        }

    }



    public void Import() {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Scanner input = null;
            try {
                input = new Scanner(file);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            int V;
            assert input != null;
            V = input.nextInt();
            while (V > 0) {
                String name = input.next();
                int x = input.nextInt();
                int y = input.nextInt();
                Vertex v1 = new Vertex(x, y, name);
                listv.add(v1);
                V--;
            }
            int E;
            E = input.nextInt();
            while (E > 0) {
                String from = input.next();
                String to = input.next();
                int wt = input.nextInt();
                Edge e1 = new Edge(from, to, wt);
                Edge e2 = new Edge(to, from, wt);
                liste.add(e1);
                liste.add(e2);
                E--;
            }
        }

    }

    public void Export() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int value = fileChooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                MyCompare com = new MyCompare();
                Collections.sort(liste,com);
                Collections.sort(listv, (vertex, t1) -> vertex.getName().compareTo(t1.getName()));
                printWriter.printf("%d\r\n", listv.size());
                for(Vertex v:listv) {
                    printWriter.printf("%s %d %d\r\n",v.getName(),v.getX_coordinate(),v.getY_coordinate());
                }
                printWriter.printf("%d\r\n", liste.size());
                for(Edge e:liste) {
                    printWriter.printf("%s %s %d\r\n",e.getFrom(),e.getTo(),e.getWt());
                }
                fileWriter.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }


    private int min(int dis[], int vis[], int V) {
        int temp = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < V; i++) {
            if (vis[i] == 0 && dis[i] < temp) {
                temp = dis[i];
                index = i;
            }
        }
        return index;
    }

    public void dijkstra()
    {
        String source = print_from.getText();
        String des = print_to.getText();
        if(source.isEmpty() || des.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EMPTY INPUT");
            alert.setContentText("Please Enter Valid Input");
            alert.showAndWait();
            return;
        }
        int f = 0;
        for(Vertex v : listv)
        {
            if(v.getName().equals(source) || v.getName().equals(des))
            {
                f++;
            }
        }
        if(f<2)
        {
            add_from.clear();
            add_to.clear();
            add_wt.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("VERTEX ENTERED DOES NOT EXIST");
            alert.setContentText("Please Enter Correct Vertices");
            alert.showAndWait();
            return;
        }
        Map<String,Integer> hashtable = new HashMap<>();
        int k=0;
        for(Vertex v : listv) {
            hashtable.put(v.getName(),k);
            k++;
        }
        Map<Integer,String> hashtables = new HashMap<>();
        int j=0;
        for(Vertex v : listv) {
            hashtables.put(j,v.getName());
            j++;
        }
        Map<String,LinkedList<Edge>> map = new HashMap<>();
        for(Edge e : liste) {
            if (!map.containsKey(e.getFrom()))
                map.put(e.getFrom(), new LinkedList<>());
            map.get(e.getFrom()).add(e);
        }
        int V = listv.size();
        System.out.println(V);
        int[] Cost = new int[V];
        int[] vis = new int[V];
        int[] parent = new int[V];

        for(int i=0;i<V;i++) {
            Cost[i]=Integer.MAX_VALUE;
        }
        for(int i=0;i<V;i++) {
            vis[i]=0;
        }
        Cost[hashtable.get(source)]=0;
        for(int i=0;i<V;i++) {
            parent[i]=-1;
        }
        for(int i=0;i<V;i++)
        {
            int u=min(Cost,vis,V);
            vis[u]=1;
            System.out.println(u +" " +hashtables.get(u));
            for(Edge x : map.get(hashtables.get(u)))
            {
                if(vis[hashtable.get(x.getTo())]==0 && Cost[hashtable.get(x.getTo())]>Cost[u]+x.getWt())
                {
                    System.out.println("hello");
                    Cost[hashtable.get(x.getTo())]=Cost[u]+x.getWt();
                    parent[hashtable.get(x.getTo())]=u;

                }
            }
        }
        int p= -1;
        ArrayList<String> paths = new ArrayList<>();
        paths.add(des);
        p = parent[hashtable.get(des)];
        System.out.println(hashtables.get(p));
        if(p>=0)
            paths.add(hashtables.get(p));
        if(Cost[hashtable.get(des)]==Double.MAX_VALUE) {
            System.out.println("no path exists");
            return;
        }
        while(parent[p]!=-1 ) {
            p = parent[p];
            paths.add(hashtables.get(p));
        }
        System.out.println(paths.size());
        StringBuilder s = new StringBuilder();
        for(int i=paths.size()-1;i>=0;i-- ) {
            s.append(paths.get(i)).append(" ");
            print_path.setText(s.toString());
        }
        for(Edge e : liste) {
            System.out.println(e.getFrom() + " "+ e.getWt() + " "+e.getTo());
        }
    }


    public void dijkstra2()
    {
        ArrayList<MyEdge> listl1 = new ArrayList<>(listl);
        //ArrayList<MyEdge> temp = new ArrayList<>();
        for(MyEdge m : listl)
        {
            MyEdge x = new MyEdge(m.getWt());
            x.setFrom(m.getTo());
            x.setTo(m.getFrom());
            listl1.add(x);
        }
        String source = nFrom.getText();
        String des = nTo.getText();
        if(source.isEmpty() || des.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("EMPTY INPUT");
            alert.setContentText("Please Enter Valid Input");
            alert.showAndWait();
            return;
        }
        int f = 0;
        for(MyCircle c : listc)
        {
            if(c.getName().equals(source) || c.getName().equals(des))
            {
                f++;
            }
        }
        if(f<2)
        {

            /*add_to.clear();
            add_wt.clear();*/
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("VERTEX ENTERED DOES NOT EXIST");
            alert.setContentText("Please Enter Correct Vertices");
            alert.showAndWait();
            return;
        }
        Map<String,Integer> hashtable = new HashMap<>();
        int k=0;
        for(MyCircle c : listc) {
            hashtable.put(c.getName(),k);
            k++;
        }
        Map<Integer,String> hashtables = new HashMap<>();
        int j=0;
        for(MyCircle c : listc) {
            hashtables.put(j,c.getName());
            j++;
        }
        Map<String,LinkedList<MyEdge>> map = new HashMap<>();
        for(MyEdge e : listl1) {
            if (!map.containsKey(e.getFrom()))
                map.put(e.getFrom(), new LinkedList<>());
            map.get(e.getFrom()).add(e);
        }
        int V = listc.size();
        System.out.println(V);
        int[] Cost = new int[V];
        int[] vis = new int[V];
        int[] parent = new int[V];
        Map<Pair<Integer,Integer>,MyEdge> roadMap = new HashMap<>();

        for(int i=0;i<V;i++) {
            Cost[i]=Integer.MAX_VALUE;
        }
        for(int i=0;i<V;i++) {
            vis[i]=0;
        }
        Cost[hashtable.get(source)]=0;
        for(int i=0;i<V;i++) {
            parent[i]=-1;
        }
        /*for()*/
        for(int i=0;i<V;i++)
        {
            int u=min(Cost,vis,V);
            vis[u]=1;
            System.out.println(u +" " +hashtables.get(u));
            for(MyEdge x : map.get(hashtables.get(u)))
            {
                if(vis[hashtable.get(x.getTo())]==0 && Cost[hashtable.get(x.getTo())]>Cost[u]+x.getWt())
                {
                    System.out.println("hello");
                    Cost[hashtable.get(x.getTo())]=Cost[u]+x.getWt();
                    parent[hashtable.get(x.getTo())]=u;
                    roadMap.put(new Pair <Integer, Integer>(u,hashtable.get(x.getTo())),x);
                }
            }
        }
        int p= -1;
        mpath = new ArrayList<>();
        mpath.add(hashtable.get(des));
        p = parent[hashtable.get(des)];
        //System.out.println(hashtables.get(p));
        if(p>=0)
            mpath.add(p);
        if(Cost[hashtable.get(des)]==Double.MAX_VALUE) {
            System.out.println("no path exists");
            return;
        }
        while(parent[p]!=-1 ) {
            p = parent[p];
            mpath.add(p);
        }
        System.out.println(source+" "+des);
        for(int i = mpath.size()-1;i>=0;i--)
        {
            for(MyCircle c : listc)
            {
                if(c.getName().equals(hashtables.get(mpath.get(i))))
                {
                    pathvertex.add(c);
                    System.out.println(c.getName());
                }
            }
        }
        for(int i= mpath.size()-1;i>0;i--)
        {
            MyEdge x = roadMap.get(new Pair<Integer, Integer>(mpath.get(i),mpath.get(i-1)));
            for(MyEdge m : listl)
            {
                if((x.getFrom().equals(m.getFrom())&&x.getTo().equals(m.getTo()))||x.getTo().equals(m.getFrom())&&x.getFrom().equals(m.getTo()))
                {
                    pathEdges.add(m);
                    m.setStroke(Color.RED);
                    break;
                }
            }

        }
    }
}
    class MyCompare implements Comparator<Edge>
    {
        public int compare(Edge m1, Edge m2)
        {
            if(m1.getFrom().equals(m2.getFrom()))
            {
                return m1.getTo().compareTo(m2.getTo());
            }else
            {
                return m1.getFrom().compareTo(m2.getFrom());
            }
        }
    }
