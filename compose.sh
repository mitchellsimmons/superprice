# --- MITCH ---
#ECR_DOMAIN=814975730297.dkr.ecr.us-east-1.amazonaws.com
# --- BEN ---
ECR_DOMAIN=547883578349.dkr.ecr.us-east-1.amazonaws.com

docker-compose -f compose-build-for-ecr.yml build api
docker-compose -f compose-build-for-ecr.yml up -d api
docker-compose -f compose-build-for-ecr.yml build web
docker tag superprice/api $ECR_DOMAIN/superprice/api
docker tag superprice/web $ECR_DOMAIN/superprice/web
docker push $ECR_DOMAIN/superprice/web
docker push $ECR_DOMAIN/superprice/api