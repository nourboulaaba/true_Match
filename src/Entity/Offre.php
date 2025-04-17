<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity]
class Offre
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 255)]
    private string $titre;

    #[ORM\Column(type: "string", length: 500)]
    private string $description;

    #[ORM\OneToOne(mappedBy: 'offre', targetEntity: Recrutement::class)]
    private ?Recrutement $recrutement = null;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getTitre()
    {
        return $this->titre;
    }

    public function setTitre($value)
    {
        $this->titre = $value;
    }

    public function getDescription()
    {
        return $this->description;
    }

    public function setDescription($value)
    {
        $this->description = $value;
    }
    public function getRecrutement(): ?Recrutement
    {
        return $this->recrutement;
    }

    public function setRecrutement(?Recrutement $recrutement): void
    {
        $this->recrutement = $recrutement;
    }
    public function __toString(): string
    {
        return $this->titre ;
    }


}
