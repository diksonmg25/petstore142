public class Pet {
    // Estamos definindo uma classe chamada Pet para guardar a estrutura de dados
    // sobre os animais

    public int id;
    public class Category{ // criada uma miniclasse - campos compostos
        public int id;
        public String name;
    }
    public Category category;
    public String name;
    public String[] photoUrls; // lista -- []
    public class Tag{ // descreve uma tag -- miniclasse - campos compostos
        public int id;
        public String name;
    }
    public Tag tags[]; // variavel criada "tags" para ser uma lista de tags
    public String status;
    
}
