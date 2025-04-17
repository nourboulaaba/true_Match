<?php

namespace App\Repository;

use App\Entity\Recrutement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class RecrutementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Recrutement::class);
    }

    public function findBySearchQuery(string $query): array
    {
        return $this->createQueryBuilder('r')

            ->Where('r.dateDebut LIKE :query')
            ->orWhere('r.dateFin LIKE :query')
            ->setParameter('query', '%'.$query.'%')
            ->orderBy('r.dateDebut', 'DESC')
            ->getQuery()
            ->getResult();
    }

    // Add custom methods as needed
}