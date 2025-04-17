<?php

namespace App\Controller;

use App\Repository\EntretienRepository;
use App\Repository\RecrutementRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class FrontController extends AbstractController
{
    #[Route('/front', name: 'app_front')]
    public function index(): Response
    {
        return $this->render('front/index.html.twig', [
            'controller_name' => 'FrontController',
        ]);
    }
    #[Route('/positions', name: 'front_positions')]
    public function positions(RecrutementRepository $recrutementRepository): Response
    {
        return $this->render('front/recrutement_list.html.twig', [
            'recrutements' => $recrutementRepository->findAll(),
        ]);
    }

    #[Route('/my-interviews', name: 'front_my_interviews')]
    public function myInterviews(EntretienRepository $entretienRepository): Response
    {
        // For testing purposes - hardcode user ID 1
        $userId = 1;

        return $this->render('front/user_entretiens.html.twig', [
            'entretiens' => $entretienRepository->findBy(['userId' => $userId]),
        ]);
    }
}
