package ma.octo.assignement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DepositDto {

  private BigDecimal Montant;
  private Date dateExecution;
  private String nom_prenom_emetteur;
  private String rib;
  private String motifDeposit;

  public BigDecimal getMontant() {
    return Montant;
  }

  public void setMontant(BigDecimal montant) {
    Montant = montant;
  }

  public Date getDateExecution() {
    return dateExecution;
  }

  public void setDateExecution(Date dateExecution) {
    this.dateExecution = dateExecution;
  }

  public String getNom_prenom_emetteur() {
    return nom_prenom_emetteur;
  }

  public void setNom_prenom_emetteur(String nom_prenom_emetteur) {
    this.nom_prenom_emetteur = nom_prenom_emetteur;
  }

  public String getRib() {
    return rib;
  }

  public void setRib(String rib) {
    this.rib = rib;
  }

  public String getMotifDeposit() {
    return motifDeposit;
  }

  public void setMotifDeposit(String motifDeposit) {
    this.motifDeposit = motifDeposit;
  }
}
