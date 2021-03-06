package ua.netcrackerteam.DAO.Entities;


import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Maksym Zhokha
 */
@Entity
@Table(name="ADVERT_CATEGORY")
public class AdvertCategory implements Serializable {
    private static final long serialVersionUID = -3254111777751188881L;    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "advert_category_seq_gen")
    @SequenceGenerator(name = "advert_category_seq_gen", sequenceName = "advert_category_seq")
    @Column (name = "ID_ADVERT_CATEGORY")
    private int idAdvertCategory;
    
    @Column(name="DESCRIPTION")
    private String description;    

    public AdvertCategory() {        
    }

    public int getIdAdvertCategory() {
        return idAdvertCategory;
    }

    public void setIdAdvertCategory(Integer idAdvertCategory) {
        this.idAdvertCategory = idAdvertCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }   
    
    
    
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvertCategory)) return false;

        AdvertCategory that = (AdvertCategory) o;

        if (idAdvertCategory != that.idAdvertCategory) return false;
//        if (adverts != null ? !adverts.equals(that.adverts) : that.adverts != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idAdvertCategory;
        result = 31 * result + (description != null ? description.hashCode() : 0);
//        result = 31 * result + (adverts != null ? adverts.hashCode() : 0);
        return result;
    }
}
