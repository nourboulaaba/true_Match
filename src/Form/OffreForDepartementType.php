<?php
// src/Form/OffreForDepartementType.php
namespace App\Form;

use App\Entity\Offre;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class OffreForDepartementType extends AbstractType
{
public function buildForm(FormBuilderInterface $builder, array $options): void
{
$builder
->add('titre')
->add('description')
->add('salaireMin')
->add('salaireMax')
// No department field, since it is passed via the controller and is automatically set.
;
}

public function configureOptions(OptionsResolver $resolver): void
{
$resolver->setDefaults([
'data_class' => Offre::class,
]);
}
}
