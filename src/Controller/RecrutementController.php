<?php

namespace App\Controller;

use App\Entity\Entretien;
use App\Entity\Offre;
use App\Entity\Recrutement;
use App\Form\EntretienType;
use App\Form\OffreType;
use App\Form\RecrutementType;
use App\Repository\OffreRepository;
use App\Repository\RecrutementRepository;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/recrutement')]
class RecrutementController extends AbstractController
{
    #[Route('/', name: 'app_recrutement_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager, OffreRepository $offreRepository): Response
    {
        $recrutements = $entityManager
            ->getRepository(Recrutement::class)
            ->findAll();

        $offres = $offreRepository->findAll();

        return $this->render('recrutement/index.html.twig', [
            'recrutements' => $recrutements,
            'offres' => $offres,
        ]);
    }

    #[Route('/new', name: 'app_recrutement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $recrutement = new Recrutement();
        $form = $this->createForm(RecrutementType::class, $recrutement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($recrutement);
            $entityManager->flush();

            return $this->redirectToRoute('app_recrutement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('recrutement/new.html.twig', [
            'recrutement' => $recrutement,
            'form' => $form,
        ]);
    }

    #[Route('/search', name: 'app_recrutement_search', methods: ['GET'])]
    public function search(Request $request, RecrutementRepository $recrutementRepository,OffreRepository $offreRepository): Response
    {

        $query = $request->query->get('q', '');
        $recrutements = $recrutementRepository->findBySearchQuery($query);
        $offres = $offreRepository->findAll();


        // For AJAX requests, return only the table HTML
        if ($request->isXmlHttpRequest()) {
            return $this->render('recrutement/_table.html.twig', [
                'recrutements' => $recrutements,
                'offres' => $offres,
            ]);
        }

        // For regular requests, return full page
        return $this->render('recrutement/_table.html.twig', [
            'recrutements' => $recrutements,
            'offres' => $offres,
        ]);
    }

    #[Route('/{id}/entretiens', name: 'app_recrutement_entretiens')]
    public function entretiens(Recrutement $recrutement, Request $request, EntityManagerInterface $em,OffreRepository $offreRepository): Response
    {
        $entretien = new Entretien();
        $form = $this->createForm(EntretienType::class, $entretien);
        $offres=$offreRepository->findAll();

        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entretien->setRecrutement($recrutement);
            $em->persist($entretien);
            $em->flush();

            return $this->redirectToRoute('app_recrutement_entretiens', ['id' => $recrutement->getId()]);
        }

        return $this->render('recrutement/entretiens.html.twig', [
            'recrutement' => $recrutement,
            'entretiens' => $recrutement->getEntretiens(),
            'form' => $form->createView(),
            'offres' => $offres,
        ]);
    }

    #[Route('/{id}', name: 'app_recrutement_show', methods: ['GET'])]
    public function show(Recrutement $recrutement,OffreRepository $offreRepository): Response
    {
        return $this->render('recrutement/show.html.twig', [
            'recrutement' => $recrutement,
            'offres' => $offreRepository->findAll(),
        ]);
    }

    #[Route('/{id}/edit', name: 'app_recrutement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Recrutement $recrutement, EntityManagerInterface $entityManager,OffreRepository $offreRepository): Response
    {
        $form = $this->createForm(RecrutementType::class, $recrutement);
        $form->handleRequest($request);
        $offres=$offreRepository->findAll();


        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_recrutement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('recrutement/editt.html.twig', [
            'recrutement' => $recrutement,
            'form' => $form,
            'offres' => $offres,
        ]);
    }

    #[Route('/{id}', name: 'app_recrutement_delete', methods: ['POST'])]
    public function delete(Request $request, Recrutement $recrutement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$recrutement->getId(), $request->request->get('_token'))) {
            $entityManager->remove($recrutement);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_recrutement_index', [], Response::HTTP_SEE_OTHER);
    }


}
