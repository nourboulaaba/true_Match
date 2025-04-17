<?php

namespace App\Form;

use App\Entity\Entretien;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EntretienType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder

            ->add('userId')
            ->add('date', DateType::class, [
                'widget' => 'single_text',  // Set widget to 'text' to use a custom input field
                'html5' => true,    // Disable the native datepicker
                'attr' => [
                    'class' => 'form-control flatpickr-date',
                    'id' => 'entretien_date', // Ensure the ID matches the selector in the JS
                ]
            ])

            ->add('lieu')
            ->add('longitude')
            ->add('latitude')
            ->add('approved')
            ->add('recrutement')
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Entretien::class,
        ]);
    }
}
