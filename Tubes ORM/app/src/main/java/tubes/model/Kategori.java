package tubes.model;

import java.util.Objects;

public class Kategori {

    private int id;
    private String nama;

    public Kategori(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public Kategori(String nama) {
        this(0, nama);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kategori)) {
            return false;
        }
        Kategori other = (Kategori) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nama;
    }
}
